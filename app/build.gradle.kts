plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.chat.joycom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chat.joycom"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // 修改apk, aab 輸出名稱
        setProperty("archivesBaseName", "${applicationId}-v${versionName}(${versionCode})")
    }

    signingConfigs {
        create("release") {
            keyAlias = "joycom"
            keyPassword = "joycom"
            storeFile = file("../gradle/jks/joycom.jks")
            storePassword = "joycom"
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
        }
    }
//    flavorDimensions += "env"
//    productFlavors {
//        create("prod") {
//            dimension = "env"
//        }
//        create("qat") {
//            dimension = "env"
//            applicationIdSuffix = ".qat"
//        }
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.core:core-ktx:+")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    val lifecycle_version = "2.6.2"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")


    val okhttp_version = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")

    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")

    val moshi_version = "1.15.0"
    implementation("com.squareup.moshi:moshi:$moshi_version")
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")

    val coil_version = "2.4.0"
    implementation("io.coil-kt:coil-compose:$coil_version")

    val hilt_version by extra { "2.48.1" }
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")

    val nav_version = "2.7.5"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    val timber_version = "5.0.1"
    implementation("com.jakewharton.timber:timber:$timber_version")

    val compose_permission_version = "0.32.0"
    implementation("com.google.accompanist:accompanist-permissions:$compose_permission_version")

    val ccp_version = "1.0.5"
    implementation ("com.github.UtkuGlsvn:ComposeCountryCodePicker:${ccp_version}")

    val datastore_version = "1.0.0"
    implementation ("androidx.datastore:datastore-preferences:$datastore_version")
    implementation ("androidx.datastore:datastore-preferences-core:$datastore_version")
    implementation ("androidx.datastore:datastore:$datastore_version")
    implementation ("androidx.datastore:datastore-core:$datastore_version")

    val room_version = "2.6.1"
    implementation ("androidx.room:room-ktx:$room_version")
    implementation ("androidx.room:room-runtime:$room_version")
    ksp ("androidx.room:room-compiler:$room_version")
    // optional - Paging 3 Integration
    implementation ("androidx.room:room-paging:$room_version")
}