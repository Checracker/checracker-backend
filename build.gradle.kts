import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.checracker"
version = "0.0.1-SNAPSHOT"

plugins {
    val kotlinVersion = "1.6.21"
    val springBootVersion = "2.7.8"

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version "1.0.15.RELEASE"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
//    kotlin("kapt") version kotlinVersion
//    kotlin("plugin.serialization") version kotlinVersion
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }

    apply {
        plugin("kotlin")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.plugin.spring")
        // all-open 플러그인(자동으로 open을 붙여줌) -> @Configuration class may not be final 에러 때문에 상위에 선언
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.springframework.boot:spring-boot-starter-actuator") // 스프링 헬스체크
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.bootJar {
    enabled = false
}

// application-front.yml front 모듈 resources로 이동
tasks.register("copyPrivate", Copy::class) {
    from("./backend-config") {
        include("application-front.yml")
    }
    into("./front/src/main/resources")
}

tasks.processResources {
    dependsOn("copyPrivate")
}

// core 프로젝트 설정
project(":core") {
    dependencies {
    }
}

// front 프로젝트 설정
project(":front") {
    dependencies {
        implementation(project(":core"))
    }
}

// batch 프로젝트 설정
project(":batch") {
    dependencies {
        implementation(project(":core"))
    }
}

// front 프로젝트 설정
project(":admin") {
    dependencies {
        implementation(project(":core"))
    }
}
// core에 front, batch, admin이 의존
