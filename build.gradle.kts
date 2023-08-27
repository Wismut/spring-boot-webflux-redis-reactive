plugins {
    java
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.max"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-docker-compose")
    implementation("net.datafaker:datafaker:2.0.1")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-ext:2.0.7")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-autoconfigure:3.0.2")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    annotationProcessor("org.projectlombok:lombok:1.18.28")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
