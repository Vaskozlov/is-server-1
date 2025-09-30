plugins {
    id("java")
    id("war")
    kotlin("jvm")
}

group = "org.vaskozov.is.lab1"
version = "1.0"

repositories {
    mavenCentral()
}

val jakartaServletVersion = "6.1.0"
val jakartaEjbVersion = "4.0.1"
val jakartaApiVersion = "10.0.0"
val hibernateVersion = "6.6.3.Final"
val postgresqlVersion = "42.7.4"
val lombokVersion = "1.18.34"
val guavaVersion = "33.3.1-jre"
val jjwtVersion = "0.12.6"
val jsonBindVersion = "3.0.1"

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:${jakartaServletVersion}")
    implementation("jakarta.ejb:jakarta.ejb-api:${jakartaEjbVersion}")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.hibernate:hibernate-core:${hibernateVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    implementation("com.google.guava:guava:${guavaVersion}")
    implementation("jakarta.platform:jakarta.jakartaee-api:${jakartaApiVersion}")
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
    implementation("jakarta.json.bind:jakarta.json.bind-api:${jsonBindVersion}")
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(17)
}