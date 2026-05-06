plugins {
    id("java")
}

// Root project — build tasks delegated to subprojects
subprojects {
    repositories {
        mavenCentral()
    }
}
