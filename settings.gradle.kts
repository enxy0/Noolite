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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Noolite"
include(":app")
include(":core:common")
include(":core:database")
include(":core:model")
include(":core:network")
include(":data")
include(":data:home")
include(":data:script")
include(":data:settings")
include(":domain:common")
include(":domain:detail")
include(":domain:home")
include(":domain:script")
include(":domain:settings")
include(":feature:detail")
include(":feature:home")
include(":feature:script")
include(":feature:settings")
include(":core:ui")
