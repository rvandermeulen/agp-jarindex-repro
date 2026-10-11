plugins {
    id("com.android.application")
}

android {
    namespace = "org.example.repro"
    compileSdk { version = release(37) { minorApiLevel = 2 } }

    defaultConfig {
        minSdk = 26
        targetSdk = 37
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.17")
    // Any dependency jar with a META-INF/INDEX.LIST works; objenesis is pulled in by Mockito in real projects.
    testImplementation("org.objenesis:objenesis:3.4")
}
