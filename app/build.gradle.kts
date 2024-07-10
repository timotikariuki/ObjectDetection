import de.undercouch.gradle.tasks.download.Download

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("de.undercouch.download")
}

// Define ASSET_DIR and TEST_ASSETS_DIR directly in your build.gradle.kts file
val ASSET_DIR = "${projectDir}/src/main/assets"
val TEST_ASSETS_DIR = "${projectDir}/src/androidTest/assets"

android {
    namespace = "com.test.objectdetection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.objectdetection"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    androidResources  {
        noCompress.add("tflite")
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)

    implementation(libs.tensorflow.lite.task.vision.play.services)
    implementation(libs.play.services.tflite.gpu)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.camera2)

    // Navigation library
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Jetpack Compose dependencies
    implementation(libs.ui)
    implementation(libs.androidx.material)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    androidTestImplementation(libs.ui.test.junit4)

}

// Download default models; if you wish to use your own models then
// place them in the "assets" directory and comment out this line.
tasks.register<Download>("downloadModelFile") {
    src("https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android_play_services/lite-model_ssd_mobilenet_v1_1_metadata_2.tflite")
    dest(file("$ASSET_DIR/mobilenetv1.tflite"))
    overwrite(false)
}

tasks.register<Download>("downloadModelFile0") {
    src("https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android_play_services/lite-model_efficientdet_lite0_detection_metadata_1.tflite")
    dest(file("$ASSET_DIR/efficientdet-lite0.tflite"))
    overwrite(false)
}

tasks.register<Download>("downloadModelFile1") {
    src("https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android_play_services/lite-model_efficientdet_lite1_detection_metadata_1.tflite")
    dest(file("$ASSET_DIR/efficientdet-lite1.tflite"))
    overwrite(false)
}

tasks.register<Download>("downloadModelFile2") {
    src("https://storage.googleapis.com/download.tensorflow.org/models/tflite/task_library/object_detection/android_play_services/lite-model_efficientdet_lite2_detection_metadata_1.tflite")
    dest(file("$ASSET_DIR/efficientdet-lite2.tflite"))
    overwrite(false)
}

tasks.register<Copy>("copyTestModel") {
    dependsOn("downloadModelFile")
    from(file("$ASSET_DIR/mobilenetv1.tflite"))
    into(file(TEST_ASSETS_DIR))
}

tasks.named("preBuild") {
    dependsOn("downloadModelFile", "downloadModelFile0", "downloadModelFile1", "downloadModelFile2", "copyTestModel")
}