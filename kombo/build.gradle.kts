plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.kombo"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

val assembleFatAar = tasks.register<Zip>("assembleFatAar") {
    dependsOn(":kombo:one:assembleRelease", ":kombo:two:assembleRelease")

    archiveBaseName.set("fat-parent")
    archiveExtension.set("aar")

    // 2) Unzip each submodule AAR
    from(zipTree(project(":kombo:one").buildDir.resolve("outputs/aar/${project(":kombo:one").name}-release.aar")))
    from(zipTree(project(":kombo:two").buildDir.resolve("outputs/aar/${project(":kombo:two").name}-release.aar")))

    // 3) Include your parent’s AndroidManifest, resources, JNI, etc.
    from("src/main/AndroidManifest.xml")
    from("src/main/res") { into("res") }
    from("src/main/jni") { into("jni") }
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // use our zip task as the AAR artifact
                artifact(assembleFatAar) {
                    builtBy(assembleFatAar)
                }
                // sourcesJar is added automatically by withSourcesJar()

                groupId    = "com.github.YOUR_USER"
                artifactId = "fat-parent"
                version    = "2.1.0"

                // tell Maven this is an AAR, not a plain jar
                pom.withXml {
                    asNode().appendNode("packaging", "aar")
                }
            }
        }
        repositories {
            maven {
                name = "GitHubPackagesTest"
                url  = uri("https://maven.pkg.github.com/IResetic/testgithubpackages")
                credentials {
                    username = project.findProperty("gpr.user") as String
                    password = project.findProperty("gpr.key")  as String
                }
            }
        }
    }
}

/*
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId    = "com.github.kombo"
                artifactId = "kombo"
                version    = "2.0.0"
            }
        }
        repositories {
            maven {
                name = "GitHubPackagesTest"
                url  = uri("https://maven.pkg.github.com/IResetic/testgithubpackages")
                credentials {
                    username = project.findProperty("gpr.user") as String
                    password = project.findProperty("gpr.key")  as String
                }
            }
        }
    }
}
*/

/*
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.example.kombo"
                artifactId = "kombo"
                version = "1.2.0"
            }
        }

        repositories {
            maven {
                name = "GitHubPackagesTest"
                url  = uri("https://maven.pkg.github.com/IResetic/testgithubpackages")
                credentials {
                    username = project.findProperty("gpr.user") as String
                    password = project.findProperty("gpr.key")  as String
                }
            }
        }
    }
}
*/
