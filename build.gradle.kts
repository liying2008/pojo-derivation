plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.7.10" apply false
    id("org.jetbrains.dokka") version "1.7.10" apply false
    id("com.vanniktech.maven.publish") version "0.23.1" apply false
}

group = ext["LIB_GROUP"]!!
version = ext["LIB_VERSION"]!!


subprojects {
    project.buildDir = File(rootProject.buildDir.absolutePath + "/" + project.name)
}

tasks.register("cleanAll", Delete::class.java) {
    delete(rootProject.buildDir)
    subprojects {
        delete("out")
    }
}

allprojects {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        mavenCentral()
    }
}
