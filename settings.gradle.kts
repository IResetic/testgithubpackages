pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TestGithubPackages"
include(":app")
include(":testlib")
include(":statistictest")
include(":combine")
include(":test")
include(":combine:libone")
include(":multilib")
include(":multilib:libone")
include(":multilib:libtwo")
include(":kombo")
include(":kombo:one")
include(":kombo:two")
