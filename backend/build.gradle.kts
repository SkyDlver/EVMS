val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgres_version: String by project

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("io.ktor.plugin") version "3.3.2"
}

group = "team.mediagroup"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

configurations.all {
    exclude(group = "org.gradle", module = "gradle-logging")
    exclude(group = "org.slf4j", module = "slf4j-simple")
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("io.ktor:ktor-server-status-pages:${ktor_version}")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("at.favre.lib:bcrypt:0.10.2")
    implementation("io.ktor:ktor-server-cors:${ktor_version}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${ktor_version}")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("io.insert-koin:koin-ktor:3.5.0")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.0")
    testImplementation("io.ktor:ktor-server-test-host:${ktor_version}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}


// --- frontend build & copy tasks ---
val frontendDir = file("../frontend")
val frontendDist = file("$frontendDir/dist")
val targetStatic = file("src/main/resources/static")

// Helper: check if dist exists
fun distExists() = frontendDist.exists() && frontendDist.listFiles()?.isNotEmpty() == true

tasks.register<Exec>("npmInstallFrontend") {
    workingDir = frontendDir
    onlyIf { !distExists() }
    // Use npm ci if package-lock.json exists, else npm install
    commandLine = if (file("${frontendDir.path}/package-lock.json").exists()) listOf("npm", "ci") else listOf("npm", "install")
}

tasks.register<Exec>("buildFrontend") {
    dependsOn("npmInstallFrontend")
    workingDir = frontendDir
    onlyIf { !distExists() }
    commandLine = listOf("npm", "run", "build")
}

tasks.register<Copy>("copyFrontend") {
    dependsOn("buildFrontend")
    from(frontendDist)
    into(targetStatic)
    // clean target before copying
    doFirst {
        if (targetStatic.exists()) targetStatic.deleteRecursively()
        targetStatic.mkdirs()
    }
}

// Ensure resources include the built frontend
tasks.named("processResources") {
    dependsOn("copyFrontend")
}
