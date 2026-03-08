pluginManagement {
    repositories {
        mavenCentral() // 최신 플러그인을 위해 최상단 배치
        google()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "IdiomQuiz"
include(":app")
include(":core")
include(":data")
include(":domain")
include(":feature:quiz")
include(":feature:result")
