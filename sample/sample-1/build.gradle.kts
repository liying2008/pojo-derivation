import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm")
    kotlin("kapt")
}

group = ext["LIB_GROUP"]!!
version = ext["LIB_VERSION"]!!

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(project(":pojo-derivation-annotations"))
    kapt(project(":pojo-derivation-compiler"))
    implementation(project(":sample:sample-base"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation(group = "junit", name = "junit", version = "4.12")
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
