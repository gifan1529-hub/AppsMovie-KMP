import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.insert-koin:koin-android:4.0.0")
            implementation("io.ktor:ktor-client-okhttp:2.3.12")

            // export pdf
            implementation("com.tom-roush:pdfbox-android:2.0.27.0")
        }
        commonMain.dependencies {
            val room_version = "2.7.0-alpha11"

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            // VIEWMODEL
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.8.2")

            // ROOM
            implementation("androidx.room:room-runtime:2.7.0-alpha11")
            implementation("androidx.sqlite:sqlite-bundled:2.5.0-alpha01")

            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
            implementation("io.ktor:ktor-client-logging:2.3.12")

            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")

            implementation("com.squareup.okio:okio:3.9.0")
            implementation("androidx.datastore:datastore-preferences-core:1.1.1")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha02") // atau versi terbaru

            implementation(compose.components.resources)

            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
            implementation("io.ktor:ktor-client-logging:2.3.12")

            implementation("io.insert-koin:koin-core:4.0.0")
            implementation("io.insert-koin:koin-compose:4.0.0")
            implementation("io.insert-koin:koin-compose-viewmodel:4.0.0")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

            implementation(compose.materialIconsExtended)

            implementation("io.github.aakira:napier:2.7.1")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

//            implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
//            implementation("io.coil-kt.coil3:coil-network-ktor:3.0.0-rc01")
        }

        iosMain.dependencies{
            implementation("io.ktor:ktor-client-darwin:2.3.12")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.moviekmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.moviekmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    val room_version = "2.7.0-alpha11"
    add("kspCommonMainMetadata", "androidx.room:room-compiler:$room_version")
    add("kspAndroid", "androidx.room:room-compiler:$room_version")
    add("kspIosArm64", "androidx.room:room-compiler:$room_version")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:$room_version")
}

