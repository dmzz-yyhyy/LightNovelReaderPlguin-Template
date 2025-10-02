import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.plugin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.plugin"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.jsoup)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.runtime)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.foundation.layout)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)

    //LNR Api
    implementation(libs.lightnovelreader.api)
}