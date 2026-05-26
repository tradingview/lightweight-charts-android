package com.tradingview.lightweightcharts.build

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.ProjectLayout
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

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
