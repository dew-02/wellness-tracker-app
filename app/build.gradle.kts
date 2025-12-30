plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.wellnesstracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.wellnesstracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // ✅ Add this block
    buildFeatures {
        viewBinding = true
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // WorkManager for hydration reminders
    implementation("androidx.work:work-runtime-ktx:2.8.1")
// Gson for transforming list objects to JSON (SharedPreferences)
    implementation("com.google.code.gson:gson:2.10.1")
// MPAndroidChart for mood trend (advanced feature)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

// Lifecycle / Fragment
    implementation("androidx.fragment:fragment-ktx:1.5.6")

// CardView
    implementation("androidx.cardview:cardview:1.0.0")



}