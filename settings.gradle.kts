rootProject.name = "checracker-backend"

include("admin")
include("batch")
include("core")
include("front")

pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }
}
