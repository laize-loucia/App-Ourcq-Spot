val MAPTILER_API_KEY: String = "9zzN7nvsweC0cL7QY6k9"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "1.9.0"
}

/*repositories {
    mavenCentral()
}*/

android {
    namespace = "com.ourcqspot.client"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ourcqspot.client"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // buildConfigField("String", "MAPTILER_API_KEY", "${MAPTILER_API_KEY}")
        // resValue("String", "MAPTILER_API_KEY", "")
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//    val navVersion = "2.8.4"
//    implementation("androidx.navigation:navigation-compose:$navVersion")
//    implementation("androidx.compose.material:material:1.4.2")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material)
    //implementation(libs.kotlinx.serialization.json)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation ("mysql:mysql-connector-java:8.0.33")

    implementation(libs.androidx.core.splashscreen)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")


    implementation("org.maplibre.gl:android-sdk:11.5.1")
//    //implementation("org.maplibre.gl:android-sdk:11.5.1")
//    //implementation("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")
//
//    // MapLibre SDK compatible avec le plugin annotations
//    implementation("org.maplibre.gl:android-sdk:10.0.1")
//
//    // Plugin Annotation pour ajouter des marqueurs
//    implementation("org.maplibre.gl:android-plugin-annotation-v9:1.0.0")
    implementation(libs.play.services.location)

}