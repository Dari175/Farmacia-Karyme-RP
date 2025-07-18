plugins {
    kotlin("jvm") version "2.1.20"
}

group = "mx.com.lennux"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //Javalin
    implementation("io.javalin:javalin:6.6.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    //vue
    implementation("org.webjars.npm:vue:3.5.4")
    //vuetify
    implementation("org.webjars.npm:vuetify:3.7.0")
    //Material Design font
    implementation("org.webjars.npm:mdi__font:7.4.47")
    //Font Awesome :D
    implementation("org.webjars:font-awesome:6.5.2")
    implementation("org.webjars.npm:roboto-fontface:0.10.0")

    //object mapper
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

    //Connection pool
    implementation("com.zaxxer:HikariCP:6.0.0")

    //Firebird driver
    implementation("org.firebirdsql.jdbc:jaybird:5.0.5.java11")
    //NOT ORM
    implementation("com.github.seratch:kotliquery:1.9.0")
    //Sweet Alert
    implementation("org.webjars.npm:sweetalert2:11.11.1")

    //Crypt
    implementation("de.svenkubiak:jBCrypt:0.4.3")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}