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

rootProject.name = "MoviesApp"
include(":app")
include(":core")
include(":movie_feature")
include(":movie_feature:movie_data")
include(":movie_feature:movie_domain")
include(":movie_feature:movie_presentation")
include(":authentication")
include(":authentication:auth_data")
include(":authentication:auth_domain")
include(":authentication:auth_presentation")
