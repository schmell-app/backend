import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "no.schmell"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}
extra["springCloudVersion"] = "2021.0.4"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	//Security

	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//Lombok
	implementation("org.projectlombok:lombok")

	//Logging
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	//Other
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("commons-io:commons-io:2.11.0")

	//Sendgrid
	implementation("com.sendgrid:sendgrid-java:4.9.3")
	implementation("com.sendgrid:java-http-client:4.5.1")

	//Dev tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	//AWS
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.429")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.security:spring-security-config")

	implementation("org.flywaydb:flyway-core:7.15.0")

	//DB
	//runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	//Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	//JobRunr
	implementation("org.jobrunr:jobrunr-spring-boot-starter:5.3.3")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.getByName<Jar>("jar") {
	enabled = false
}
tasks.withType<Test> {
	useJUnitPlatform()
}
