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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // Include submodules
    api(project(":kombo:one"))
    api(project(":kombo:two"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.example"
                artifactId = "kombo"
                version = "3.1.0"
                
                // Include submodules in the publication
                artifact(tasks.getByName("bundleReleaseAar"))
                artifact(tasks.getByName("sourcesJar"))
            }
        }
        repositories {
            maven {
                name = "GitHubPackagesTest"
                url = uri("https://maven.pkg.github.com/IResetic/testgithubpackages")
                credentials {
                    username = project.findProperty("gpr.user") as String
                    password = project.findProperty("gpr.key") as String
                }
            }
        }
    }
}

// Create sources jar task
tasks.register("sourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}
