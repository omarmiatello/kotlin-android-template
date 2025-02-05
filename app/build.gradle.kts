plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdkVersion(Sdk.compile)

    defaultConfig {
        minSdkVersion(Sdk.min)
        targetSdkVersion(Sdk.target)

        applicationId = "com.github.owner.template.app"
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = Version.java
        freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn" // for Jetpack Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.compose
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Version.java)
        targetCompatibility = JavaVersion.toVersion(Version.java)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        disable("ObsoleteLintCustomCheck")

        isWarningsAsErrors = true
        isAbortOnError = true
    }

    /**
     * Use this block to configure different flavors
     */
    // flavorDimensions("version")
    // productFlavors {
    //     create("full") {
    //         dimension = "version"
    //         applicationIdSuffix = ".full"
    //     }
    //     create("demo") {
    //         dimension = "version"
    //         applicationIdSuffix = ".demo"
    //     }
    // }
}

dependencies {
    implementation(project(":library-android"))
    implementation(project(":library-kotlin"))

    implementation(Lib.androidxAppcompat)
    implementation(Lib.androidxCoreKtx)
    LibGroup.composeAll.forEach { implementation(it) }
    LibGroup.composeDebug.forEach { debugImplementation(it) }

    testImplementation(Lib.testJunit)

    LibGroup.testAndroid.forEach { androidTestImplementation(it) }
}
