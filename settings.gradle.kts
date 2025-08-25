rootProject.name = "Fabulous-chocolate-factory"

// Incluindo módulos
//include(":Ingredient-store")
//include(":Chocolate-factory")      // exemplo de outro módulo futuro
//include(":Order-service")           // outro módulo opcional

// Configuração de buildScan / pluginManagement (opcional)
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

// Repositórios padrão para todos os projetos
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}