import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

repositories { mavenCentral() }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // ✅ NoSQL: MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // API 문서
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // 선택: Redis(레이트리밋/토큰블랙리스트 등 필요 시)
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // 선택: JWT(다음 단계에서 사용할 예정)
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test { useJUnitPlatform() }
