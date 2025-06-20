import java.util.Base64

plugins {
  `maven-publish`
  signing
  kotlin("multiplatform") version "1.9.25"
  id("org.jetbrains.dokka") version "1.9.0"
  id("tech.yanand.maven-central-publish") version "1.3.0"
}

group = "io.k-libs"
version = "0.1.1"
description = "Pure Kotlin BigInt and BigDec implementations"

repositories {
  mavenCentral()
}

kotlin {
  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
    withJava()
    java {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }

  js(IR) {
    browser()
    nodejs()
    binaries.executable()

    compilations.all {
      packageJson {
        customField("description", project.description!!)
      }
    }
  }

  val hostOs = System.getProperty("os.name")
  val isMingwX64 = hostOs.startsWith("Windows")
  val nativeTarget = when {
    hostOs == "Mac OS X" -> macosX64("native")
    hostOs == "Linux"    -> linuxX64("native")
    isMingwX64           -> mingwX64("native")
    else                 -> throw GradleException("Host OS is not supported in Kotlin/Native.")
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("io.k-libs:deque:0.9.0")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val jvmMain by getting
    val jvmTest by getting
    val jsMain by getting
    val jsTest by getting
    val nativeMain by getting
    val nativeTest by getting
  }
}

tasks.dokkaHtml {
  outputDirectory.set(file("docs/dokka/${project.version}"))
}

val javadocJar = tasks.register<Jar>("javadocJar") {
  dependsOn(tasks.dokkaHtml)
  archiveClassifier.set("javadoc")
  from(file("docs/dokka/${project.version}"))
}

tasks.withType<Jar> {
  enabled = true
}

tasks.withType<PublishToMavenRepository> {
  mustRunAfter(":signJvmPublication", ":signJsPublication", ":signKotlinMultiplatformPublication", ":signNativePublication")
}

mavenCentral {
  authToken = Base64.getUrlEncoder().encodeToString("${project.properties["nexus.user"]}:${project.properties["nexus.pass"]}".toByteArray())
}

publishing {
  publications {
    withType<MavenPublication> {
      artifact(javadocJar)

      pom {
        name.set("Big-Numbers")
        description.set(project.description)
        url.set("https://github.com/k-libs/k-big-numbers")

        licenses {
          license {
            name.set("MIT")
          }
        }

        developers {
          developer {
            id.set("epharper")
            name.set("Elizabeth Paige Harper")
            email.set("foxcapades.io@gmail.com")
            url.set("https://github.com/foxcapades")
          }
        }

        scm {
          connection.set("scm:git:git://github.com/k-libs/k-big-numbers.git")
          developerConnection.set("scm:git:ssh://github.com/k-libs/k-big-numbers.git")
          url.set("https://github.com/k-libs/k-big-numbers")
        }
      }
    }
  }
}

signing {
  useGpgCmd()

  sign(configurations.archives.get())
  publishing.publications.withType<MavenPublication> { sign(this) }
}
