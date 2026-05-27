import java.util.Properties
import java.io.FileInputStream

// Load local.properties at the top-level so it's available everywhere
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.anywaa.connect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.anywaa.connect"
        minSdk = 27
        targetSdk = 35
        versionCode = 99
        versionName = "3.7.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val hfToken: String = localProperties.getProperty("HF_TOKEN", "")
        buildConfigField("String", "HF_TOKEN", "\"$hfToken\"")

        val debugPremium: Boolean = localProperties.getProperty("DEBUG_PREMIUM", "false").toBoolean()
        buildConfigField("Boolean", "DEBUG_PREMIUM", "$debugPremium")

        // Keep only non-asset-delivery metadata placeholders minimal (manifest may still reference these)
        // AdMob IDs are still exposed, but you already removed the AdMob meta-data in the manifest merger step.
        val admobAppId: String = localProperties.getProperty(
            "ADMOB_APP_ID", "ca-app-pub-3940256099942544~3347511713"
        )
        buildConfigField("String", "ADMOB_APP_ID", "\"$admobAppId\"")
    }

    // ✅ androidResources is the correct block for locale/resource filtering in AGP 9.x
    androidResources {
        localeFilters += listOf(
            "en", "es", "pt", "de", "fr", "ru", "it", "tr", "pl", "ar",
            "ja", "id", "in", "ko", "fa", "he", "iw", "uk", "zh"
        )
    }

    // ✅ splits belongs directly inside android { }, NOT inside composeOptions { }
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk { debugSymbolLevel = "NONE" }
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        // composeOptions only contains kotlinCompilerExtensionVersion
        kotlinCompilerExtensionVersion = "1.5.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE-LGPL-3.txt"
            excludes += "META-INF/LICENSE-LGPL-3.0.txt"
            excludes += "META-INF/LICENSE*"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/DEPENDENCIES.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/*.kotlin_module"
            excludes += "google/protobuf/*.proto"
        }

        jniLibs {
            useLegacyPackaging = true
            pickFirsts += setOf(
                "**/libonnxruntime.so"
            )
            excludes += setOf(
                "**/libmediapipe_*.so",
                "**/libQnn*.so",
                "**/liblitertlm*.so",
                "**/libnexa_*.so",
                "**/libonnxruntime*.so",
                "**/libstable-diffusion*.so",
                "**/libdeepseek-ocr.so",
                "**/libcvtbase*.so",
                "**/libCalculator_skel.so",
                "**/libLiteRt*.so",
                "**/libPlatformValidatorShared.so",
                "**/libgemma_embedding_model_jni.so",
                "**/libgranite*.so",
                "**/libjina-rerank-sdk.so",
                "**/libliquid-sdk.so",
                "**/libllama3-3b-sdk.so",
                "**/libomni-neural-sdk.so",
                "**/libpaddle*.so",
                "**/libparakeet-sdk.so",
                "**/libphi*.so",
                "**/libqwen*.so",
                "**/librfdetr-sdk.so",
                "**/librmbg*.so",
                "**/libtable-transformer-sdk.so",
                "**/libwav2vec2-sdk.so",
                "**/armeabi-v7a/**"
            )
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("com.google.protobuf:protobuf-java:3.25.1")
    }
    exclude(group = "com.google.protobuf", module = "protobuf-javalite")
}

dependencies {
    // Core UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)

    // Navigation / state
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Networking / storage
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.coroutines.android)
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Permissions/UI helpers
    implementation(libs.accompanist.systemuicontroller)

    // Images
    implementation(libs.coil.compose)

    // JSON
    implementation(libs.gson)
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.android.material:material:1.12.0")

    // PRUNED: inference/AI stack (removed to reduce APK size)
    // - MediaPipe / ONNX Runtime / Nexa / LiteRT-LM / QNN assets
    // - RAG localagents
    // - Flexmark / iText PDF / Commons CSV

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
