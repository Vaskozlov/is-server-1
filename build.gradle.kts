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
val jakartaApiVersion = "11.0.0"
val jakartaDataVersion = "1.0.1"
val hibernateVersion = "7.1.7.Final"
val postgresqlVersion = "42.7.7"
val lombokVersion = "1.18.34"
val guavaVersion = "33.3.1-jre"
val jjwtVersion = "0.12.6"
val jsonBindVersion = "3.0.1"
val validatorVersion = "8.0.3.Final"
val jakartaPersistenceApi = "3.2.0"
val argon2Version = "2.12"
val minioVersion = "8.6.0"
val opencsvVersion = "5.12.0"

java{
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly("jakarta.servlet:jakarta.servlet-api:${jakartaServletVersion}")
    implementation("jakarta.ejb:jakarta.ejb-api:${jakartaEjbVersion}")
    implementation("jakarta.persistence:jakarta.persistence-api:${jakartaPersistenceApi}")
    implementation("jakarta.data:jakarta.data-api:${jakartaDataVersion}")
    implementation("org.postgresql:postgresql:${postgresqlVersion}")
    implementation("org.hibernate:hibernate-core:${hibernateVersion}")
    implementation("org.hibernate:hibernate-validator:${validatorVersion}")
    compileOnly("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.projectlombok:lombok:${lombokVersion}")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:${hibernateVersion}")
    implementation("com.google.guava:guava:${guavaVersion}")
    compileOnly("jakarta.platform:jakarta.jakartaee-api:${jakartaApiVersion}")
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
    implementation("jakarta.json.bind:jakarta.json.bind-api:${jsonBindVersion}")
    implementation(kotlin("stdlib-jdk8"))
    implementation("de.mkammerer:argon2-jvm:${argon2Version}")
    implementation("io.minio:minio:${minioVersion}")
    implementation("com.opencsv:opencsv:${opencsvVersion}")
}

kotlin {
    jvmToolchain(21)
}