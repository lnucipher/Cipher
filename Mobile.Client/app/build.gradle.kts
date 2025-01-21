import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.google.service)
}

android {
    namespace = "com.example.cipher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cipher"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                val capName = variant.name
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                tasks.getByName<KotlinCompile>("ksp${capName}Kotlin") {
                    setSource(tasks.getByName("generate${capName}Proto").outputs)
                }
            }
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    // MARK: - DataStore
    implementation(libs.protobuf.javalite)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore)

    // MARK: - Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // MARK: - Navigation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.compose.animation)

    // MARK: - Hilt (dagger/hilt)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // MARK: - Moshi
    implementation(libs.retrofit2.converter.moshi)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.skydoves.sandwich.retrofit)

    // MARK: - Retrofit
    implementation(libs.squareup.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // MARK: - Coil
    implementation(libs.coil.compose)

    // MARK: - SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // MARK: - Room
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation (libs.room.paging)

    // MARK: - Paging3
    implementation (libs.paging.runtime.ktx)
    implementation (libs.paging.compose)

    // MARK: - Signalr
    implementation(libs.microsoft.signalr)

    // MARK: - Glide
    implementation (libs.glide)

    // MARK: - Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messanging)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
