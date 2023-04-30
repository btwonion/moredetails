@file:Suppress("SpellCheckingInspection")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    id("fabric-loom") version "1.2-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.8.0"

    id("com.modrinth.minotaur") version "2.7.5"
    id("com.github.breadmoirai.github-release") version "2.4.1"
    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "1.0.0"
val mcVersion = "1.19.4"
version = "$majorVersion-$mcVersion"
description = "Adds useful information to your ingame hud"
val authors = listOf("btwonion")
val githubRepo = "btwonion/moredetails"

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.isxander.dev/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        parchment("org.parchmentmc.data:parchment-1.19.3:2023.03.12@zip")
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.19")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.79.0+1.19.4")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.9.4+kotlin.1.8.21")
    modImplementation("dev.isxander.yacl:yet-another-config-lib-fabric:2.5.0+1.19.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    modApi("com.terraformersmc:modmenu:6.2.0")
}

tasks {
    processResources {
        val modId = "moredetails"
        val modName = modId
        val modDescription = "Adds useful information to your ingame hud"

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubRepo)

        filesMatching("fabric.mod.json") {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubRepo,
            )
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("modrinthSyncBody")
        dependsOn("modrinth")
        dependsOn("githubRelease")
        dependsOn("publish")
    }
}
val changelogText =
    file("changelogs/$majorVersion.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("lg17V3i3")
    versionNumber.set(project.version.toString())
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf(mcVersion))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
        required.project("yacl")
        optional.project("modmenu")
    }
    changelog.set(changelogText)
    syncBodyFrom.set(file("README.md").readText())
}

githubRelease {
    token(findProperty("github.token")?.toString())

    val split = githubRepo.split("/")
    owner(split[0])
    repo(split[1])
    tagName("v${project.version}")
    body(changelogText)
    overwrite(true)
    releaseAssets(tasks["remapJar"].outputs.files)
    targetCommitish("master")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

publishing {
    repositories {
        maven {
            name = "nyon"
            url = uri("https://repo.nyon.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.nyon"
            artifactId = "moredetails"
            version = project.version.toString()
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()
}