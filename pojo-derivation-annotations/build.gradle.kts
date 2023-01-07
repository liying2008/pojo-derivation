plugins {
    java
    id("com.vanniktech.maven.publish")
}

group = ext["GROUP"]!!
version = ext["VERSION_NAME"]!!

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
