plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.multilib"
    compileSdk = 35

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
    api(project(":multilib:libone"))
    api(project(":multilib:libtwo"))
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.example.multilib"
                artifactId = "multilib"
                version = "1.1.0"
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
