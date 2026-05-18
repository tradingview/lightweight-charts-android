import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.builtin.kotlin)
    alias(libs.plugins.maven.publish)
}

extensions.configure<LibraryExtension>("android") {
    namespace = "com.tradingview.lightweightcharts"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        version = libs.versions.lightweightcharts.asProvider().get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        // Publish only the release AAR variant. This matches the Maven artifact consumed by
        // Android clients and avoids exposing debug-only sample configuration.
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

abstract class NpmBuildTask @Inject constructor(
    private val execOperations: ExecOperations,
    private val projectLayout: ProjectLayout
) : DefaultTask() {
    @TaskAction
    fun run() {
        val projectDirectory = projectLayout.projectDirectory.asFile
        if (File(projectDirectory, "package-lock.json").exists()) {
            evaluateShellScript("npm", "ci")
        } else {
            evaluateShellScript("npm", "install")
        }
        evaluateShellScript("npm", "run", "compile")
        evaluateShellScript("npm", "run", "compile-price-formatter")
        evaluateShellScript("npm", "run", "compile-time-formatter")
        evaluateShellScript("npm", "run", "compile-eval-plugin")
    }

    private fun evaluateShellScript(vararg script: String) {
        val outputStream = ByteArrayOutputStream()
        val result = execOperations.exec {
            workingDir = projectLayout.projectDirectory.asFile
            if (System.getProperty("os.name").lowercase().contains("windows")) {
                val command = script.joinToString(" ") + "; if (!\$?) { exit 1 }"
                commandLine("powershell", "-Command", command)
            } else {
                val executable = resolveExecutable(script.first())
                commandLine(listOf(executable.absolutePath) + script.drop(1))
                environment(
                    "PATH",
                    "${executable.parent}${File.pathSeparator}${System.getenv("PATH").orEmpty()}"
                )
            }
            errorOutput = outputStream
            standardOutput = outputStream
            isIgnoreExitValue = true
        }

        if (result.exitValue != 0) {
            throw GradleException(outputStream.toString())
        }
    }

    private fun resolveExecutable(command: String): File {
        val configuredExecutable = when (command) {
            "npm" -> System.getenv("NPM_EXECUTABLE")
            else -> null
        }
        val pathCandidates = System.getenv("PATH")
            .orEmpty()
            .split(File.pathSeparator)
            .filter { it.isNotBlank() }
            .map { File(it, command) }
        val nvmCandidates = File(System.getProperty("user.home"), ".nvm/versions/node")
            .listFiles()
            .orEmpty()
            .flatMap { nodeVersion -> listOf(File(nodeVersion, "bin/$command")) }
            .sortedWith { first, second -> compareNodeVersions(second, first) }
        val candidates = listOfNotNull(configuredExecutable?.let(::File)) +
            pathCandidates +
            nvmCandidates +
            listOf(
                File("/opt/homebrew/bin/$command"),
                File("/usr/local/bin/$command"),
                File("/usr/bin/$command")
            )

        return candidates.firstOrNull { it.isFile && it.canExecute() }
            ?: throw GradleException(
                "Unable to find '$command'. Install Node.js/npm or set NPM_EXECUTABLE to the npm binary path."
            )
    }

    private fun compareNodeVersions(first: File, second: File): Int {
        val firstParts = nodeVersionParts(first)
        val secondParts = nodeVersionParts(second)
        val maxSize = maxOf(firstParts.size, secondParts.size)
        for (index in 0 until maxSize) {
            val firstPart = firstParts.getOrElse(index) { 0 }
            val secondPart = secondParts.getOrElse(index) { 0 }
            if (firstPart != secondPart) {
                return firstPart.compareTo(secondPart)
            }
        }
        return 0
    }

    private fun nodeVersionParts(executable: File): List<Int> {
        return executable.parentFile.parentFile.name
            .removePrefix("v")
            .split('.')
            .mapNotNull { it.toIntOrNull() }
    }
}

val npmBuild = tasks.register<NpmBuildTask>("npmBuild") {
    inputs.file(layout.projectDirectory.file("package.json"))
    listOf("package-lock.json", "npm-shrinkwrap.json").forEach { lockfile ->
        val candidate = layout.projectDirectory.file(lockfile)
        if (candidate.asFile.exists()) {
            inputs.file(candidate)
        }
    }
    inputs.file(layout.projectDirectory.file("webpack.config.js"))
    inputs.dir(layout.projectDirectory.dir("lib"))
    outputs.dir(layout.projectDirectory.dir("src/main/assets/com/tradingview/lightweightcharts/scripts"))
}

tasks.configureEach {
    if (name.startsWith("merge") && name.endsWith("Assets")) {
        dependsOn(npmBuild)
    }
}

afterEvaluate {
    publishing {
        repositories {
            maven(url = "${layout.buildDirectory.get()}/repo")
        }

        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.tradingview"
                artifactId = "lightweightcharts"
                version = libs.versions.lightweightcharts.asProvider().get()
                pom {
                    name = "Android Lightweight Charts"
                    description = "The Android Lightweight Charts is an Android wrapper of " +
                        "the TradingView Lightweight Charts library."
                    url = "https://github.com/tradingview/lightweight-charts-android"

                    licenses {
                        license {
                            name = "The Apache License, Version 2.0"
                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        }
                    }

                    developers {
                        developer {
                            name = "Alexandr Nevyantsev"
                            email = "anevyantsev@tradingview.com"
                            organization = "TradingView"
                            organizationUrl = "https://www.tradingview.com/"
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com/tradingview/lightweight-charts-android.git"
                        developerConnection = "scm:git:ssh://github.com/tradingview/lightweight-charts-android.git"
                        url = "http://github.com/tradingview/lightweight-charts-android/tree/master"
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.webkit)
    implementation(libs.gson)

    testImplementation(libs.junit)
}
