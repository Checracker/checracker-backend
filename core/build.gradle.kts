dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // implementation 쓰면 core 상속해도 얘는 상속 안됨 -> compile 써줘야 하는데 compile 보다 api 쓰는 걸 권장
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j") // MySQL 8.0.31 버전부터 groupId와 artifactId가 변경
}

plugins {
    kotlin("plugin.jpa")
//    kotlin("kapt")
//    kotlin("plugin.serialization")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.jar { enabled = true }
tasks.bootJar { enabled = false }