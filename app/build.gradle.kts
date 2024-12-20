plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.myproyect.gestornovelasnjr"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.myproyect.gestornovelasnjr"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation", "$projectDir/schemas")
            }
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
}

dependencies {

    // Google Play Services Maps y Location
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // Librería de Geocodificación
    implementation ("com.google.android.libraries.places:places:2.6.0")

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.fragment:fragment:1.6.1")

    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation(libs.preference)
    implementation(libs.fragment)
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:4.11.0")
    testImplementation ("androidx.test:core:1.5.0")
    testImplementation ("org.robolectric:robolectric:4.9.2")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.android.gms:play-services-location:21.0.1")


    implementation("androidx.work:work-runtime:2.8.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation ("com.google.android.material:material:1.6.0")


    // Otras dependencias...
}

apply(plugin = "com.google.gms.google-services")
