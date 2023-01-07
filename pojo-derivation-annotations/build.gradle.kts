plugins {
    java
    id("com.vanniktech.maven.publish")
}

group = ext["LIB_GROUP"]!!
version = ext["LIB_VERSION"]!!

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
