import com.github.breadmoirai.GithubReleaseExtension
import org.gradle.api.tasks.Upload
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
    kotlin("jvm") version "1.3.11"
    `maven-publish`
    id("com.github.breadmoirai.github-release") version "2.2.4"
}

buildscript {
    repositories {
        mavenCentral()
            maven(url = "http://maven.fabricmc.net/") {
                name = "Fabric"
            }
    }
    dependencies {
        classpath("net.fabricmc:fabric-loom:0.2.0-SNAPSHOT")
    }
}

val framework: String? by project
val frameworkSelected = framework ?: "fabric"
if (frameworkSelected == "fabric"){
    apply<net.fabricmc.loom.LoomGradlePlugin>()
}

val BUILD_NO: String? by project
val buildNo = BUILD_NO ?: "SNAPSHOT"

val project_version: String by project
val version_mc: String by project
val version_mc_jar: String by project
val version_mc_mappings: String by project
val version_fabric: String by project
val version_fabric_loader: String by project

val changelog: String? by project
val github_release_token: String? by project
val maven_url: String? by project

version =  "3.0.0-$frameworkSelected-$buildNo"
group = "com.hea3ven.tools.commonutils"

base {
    archivesBaseName = "h3nt-commonutils"
}

repositories {
    mavenCentral()
    /*maven(url="https://raw.github.com/hea3ven/mvn-repo/master") {
        name = "Hea3veN"
    }*/
}

dependencies {
    if (frameworkSelected == "fabric") {
        "minecraft"("com.mojang:minecraft:$version_mc_jar")
        "mappings"("net.fabricmc:yarn:$version_mc_mappings")
        "modCompile"("net.fabricmc:fabric-loader:$version_fabric_loader")

        "modCompile"("net.fabricmc:fabric:$version_fabric")
    }

    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    testCompile("junit:junit:4.12")
}

if (frameworkSelected == "fabric") {
    sourceSets["main"].java.exclude("com/hea3ven/tools/commonutils/mod/forge/**")
}

tasks.register<Jar>("sourcesJar") {
    from(sourceSets["main"].allJava)
    archiveClassifier.set("sources")
}

tasks.named<JavaCompile>("compileJava") {
  sourceCompatibility = "1.8"
  targetCompatibility = "1.8"
}

tasks.named<KotlinCompile>("compileKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.named<KotlinCompile>("compileTestKotlin") {
    kotlinOptions.jvmTarget = "1.8"
}


configure<GithubReleaseExtension> {
    token(github_release_token ?: "")
    owner.set("Hea3veN")
    repo.set("CommonUtils")
    targetCommitish.set("master")
    body.set(changelog ?: "")
    draft.set(false)
    prerelease.set(false)
    releaseAssets.setFrom(tasks.jar.get().outputs.files)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "h3nt-commonutils"
            from(components["java"])
            artifact(tasks["sourcesJar"])
            pom {
                name.set("My Library")
            }
        }
    }
    repositories {
        maven {
            url = uri(maven_url ?: "file:///tmp/mvn")
        }
    }
}

