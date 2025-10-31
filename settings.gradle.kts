pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}


buildCache {
    local {
        isPush = true
        directory = File(rootDir, ".gradle/build-cache")
        removeUnusedEntriesAfterDays = 1
    }
}

rootProject.name = "LightweightCharts"


include(
    ":app",
    ":lightweightlibrary"
)
