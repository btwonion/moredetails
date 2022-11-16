@file:Suppress("SpellCheckingInspection")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.21"
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.4"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.1"

    id("com.modrinth.minotaur") version "2.4.4"
    id("com.github.breadmoirai.github-release") version "2.4.1"
    `maven-publish`
    signing
}

group = "dev.nyon"
val majorVersion = "1.0.0"
version = "$majorVersion-1.19.2"
description = "Adds an useful information to your ingame hud"
val authors = listOf("btwonion")
val githubRepo = "btwonion/moredetails"

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.19.2")
    mappings(loom.layered {
        //addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.19.2+build.21:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.66.0+1.19.2")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.8.6+kotlin.1.7.21")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    /*
    removed until ktoml support arrays

    include("com.akuleshov7:ktoml-core-jvm:0.3.0")
    implementation("com.akuleshov7:ktoml-core-jvm:0.3.0")
     */
}

tasks {
    processResources {
        val modId = "moredetails"
        val modName = "moredetails"

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", description)
        inputs.property("version", project.version)
        inputs.property("github", githubRepo)

        filesMatching(listOf("fabric.mod.json", "quilt.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to description,
                "version" to project.version,
                "github" to githubRepo,
            )
        }
    }

    register("releaseMod") {
        group = "publishing"

        dependsOn("modrinth")
        dependsOn("modrinthSyncBody")
        dependsOn("githubRelease")
        dependsOn("publish")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.freeCompilerArgs += "-Xskip-prerelease-check"
    }

    withType<JavaCompile> {
        options.release.set(17)
    }
}
val changelogText =
    file("changelogs/$majorVersion.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."

modrinth {
    token.set(findProperty("modrinth.token")?.toString())
    projectId.set("")
    versionNumber.set("${project.version}")
    versionType.set("release")
    uploadFile.set(tasks["remapJar"])
    gameVersions.set(listOf("1.19.2"))
    loaders.set(listOf("fabric", "quilt"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
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
    releaseAssets(tasks["remapJar"].outputs.files)
    targetCommitish("master")
}

publishing {
    repositories {
        maven {
            name = "ossrh"
            credentials(PasswordCredentials::class)
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
        }
    }

    publications {
        register<MavenPublication>(project.name) {
            from(components["java"])

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = rootProject.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    authors.forEach {
                        developer {
                            name.set(it)
                        }
                    }
                }

                licenses {
                    license {
                        name.set("GNU General Public License 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }

                url.set("https://github.com/${githubRepo}")

                scm {
                    connection.set("scm:git:git://github.com/${githubRepo}.git")
                    url.set("https://github.com/${githubRepo}/tree/main")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

java {
    withSourcesJar()
    withJavadocJar()
}