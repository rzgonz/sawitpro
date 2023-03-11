object LibsVersion {
    // Build tools and SDK
    const val kotlin = "1.4.31"

    // Android libraries
    const val appcompat = "1.2.0"
    const val cardview = "1.0.0"
    const val constraintlayout = "2.0.0"
    const val coordinatorLayout = "1.1.0"
    const val core = "1.7.0"
    const val fragment = "1.2.5"
    const val lifecycle = "2.3.1"
    const val navigation = "2.3.0"
    const val recyclerview = "1.1.0"
    const val material = "1.2.1"
    const val viewPager = "1.0.0"

    // Libraries
    const val autoValue = "1.6.6"
    const val epoxy = "4.0.0"
    const val koin = "3.3.2"
    const val koinCompose = "3.4.1"
    const val kotlinCoroutines = "1.4.1"
    const val lottie = "3.4.0"
    const val glide = "4.11.0"
    const val eventBus = "3.2.0"
    const val desugar = "1.1.5"

    //    const val moshi = "1.9.2"
    const val gson = "2.8.6"
    const val multidex = "2.0.1"
    const val mavericks = "2.6.0"
    const val retrofit = "2.7.2"
    const val rxAndroid = "2.1.1"
    const val rxJava = "2.2.9"
    const val okHttp3 = "3.12.1"
    const val resizeSSPSDP = "1.0.6"
    const val cameraxVersion = "1.0.0-beta12"

    // Instrumented testing libraries
    const val espresso = "3.2.0"

    // Testing libraries
    const val junit = "4.13.2"
    const val junitExt = "1.1.1"
    const val mockito = "2.25.1"
    const val mockitoKotlin = "2.2.0"
    const val mockk = "1.9.3"
    const val robolectric = "4.3.1"
    const val testCore = "1.2.0"
    const val shimmer = "0.5.0"

    //compose
    const val kotlinCompilerExtensionVersion = "1.3.2"
    const val composeActivity = "1.5.1"
    const val composeViewModel = "2.5.1"


    //debugging
    const val flipperVersion = "0.92.0"
}

object AnnotationProcessors {
    const val autoValue = "com.google.auto.value:auto-value:${LibsVersion.autoValue}"
    const val epoxy = "com.airbnb.android:epoxy-processor:${LibsVersion.epoxy}"
    const val lifecycle = "androidx.lifecycle:lifecycle-compiler:${LibsVersion.lifecycle}"
//    const val moshi = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
}

object Libraries {
    const val appcompat = "androidx.appcompat:appcompat:${LibsVersion.appcompat}"
    const val autoValue = "com.google.auto.value:auto-value-annotations:${LibsVersion.autoValue}"
    const val mvrx = "com.airbnb.android:mavericks:${LibsVersion.mavericks}"
    const val cardview = "androidx.cardview:cardview:${LibsVersion.cardview}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${LibsVersion.viewPager}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${LibsVersion.constraintlayout}"
    const val coordinatorLayout =
        "androidx.coordinatorlayout:coordinatorlayout:${LibsVersion.coordinatorLayout}"
    const val coreKtx = "androidx.core:core-ktx:${LibsVersion.core}"
    const val epoxy = "com.airbnb.android:epoxy:${LibsVersion.epoxy}"
    const val espressoIdlingResource =
        "androidx.test.espresso:espresso-idling-resource:${LibsVersion.espresso}"
    const val fragment = "androidx.fragment:fragment:${LibsVersion.fragment}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${LibsVersion.fragment}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${LibsVersion.fragment}"
    const val junit = "junit:junit:${LibsVersion.junit}"
    const val koin = "io.insert-koin:koin-android:${LibsVersion.koin}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${LibsVersion.koinCompose}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${LibsVersion.kotlin}"
    const val glide = "com.github.bumptech.glide:glide:${LibsVersion.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${LibsVersion.glide}"
    const val kotlinCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibsVersion.kotlinCoroutines}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${LibsVersion.kotlin}"
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${LibsVersion.lifecycle}"
    const val lottie = "com.airbnb.android:lottie:${LibsVersion.lottie}"

    //    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
//    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val gson = "com.google.code.gson:gson:${LibsVersion.gson}"
    const val multidex = "androidx.multidex:multidex:${LibsVersion.multidex}"
    const val navigationFragmentKtx =
        "androidx.navigation:navigation-fragment-ktx:${LibsVersion.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${LibsVersion.navigation}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${LibsVersion.recyclerview}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${LibsVersion.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${LibsVersion.retrofit}"
    const val okHttp3 = "com.squareup.okhttp3:okhttp:${LibsVersion.okHttp3}"
    const val okHttp3Log = "com.squareup.okhttp3:logging-interceptor:${LibsVersion.okHttp3}"
    const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${LibsVersion.retrofit}"
    const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${LibsVersion.lifecycle}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${LibsVersion.rxAndroid}"
    const val rxJava = "io.reactivex.rxjava2:rxjava:${LibsVersion.rxJava}"
    const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibsVersion.lifecycle}"
    const val viewModelSavedState =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${LibsVersion.lifecycle}"

    const val androidMaterial = "com.google.android.material:material:${LibsVersion.material}"

    const val textSSP = "com.intuit.ssp:ssp-android:${LibsVersion.resizeSSPSDP}"
    const val imageSDP = "com.intuit.sdp:sdp-android:${LibsVersion.resizeSSPSDP}"

    const val shimmer = "com.facebook.shimmer:shimmer:${LibsVersion.shimmer}"
    const val eventBus = "org.greenrobot:eventbus:${LibsVersion.eventBus}"
    const val desugar = "com.android.tools:desugar_jdk_libs:${LibsVersion.desugar}"

    object cameraX {
        const val cameraXcore = "androidx.camera:camera-core:${LibsVersion.cameraxVersion}"
        const val cameraXcamera2 = "androidx.camera:camera-camera2:${LibsVersion.cameraxVersion}"
        const val cameraXlifecycle =
            "androidx.camera:camera-lifecycle:${LibsVersion.cameraxVersion}"
        const val cameraXview = "androidx.camera:camera-view:1.0.0-alpha18"
    }

    const val jsoup = "org.jsoup:jsoup:1.13.1"
    // For Compose
    const val composeBom = "androidx.compose:compose-bom:2022.12.00"
    const val imageCoil = "io.coil-kt:coil-compose:2.2.2"

    const val compose_foundation =
        "androidx.compose.foundation:foundation"
    const val compose_ui = "androidx.compose.ui:ui"
    const val compose_material =
        "androidx.compose.material:material"
    const val compose_material3 =
        "androidx.compose.material3:material3"
    const val compose_material3_screen_size =
        "androidx.compose.material3:material3-window-size-class"
    const val compose_icon =
        "androidx.compose.material:material-icons-core"
    const val compose_icon_extended =
        "androidx.compose.material:material-icons-extended"

    // Android Studio Preview support
    const val compose_tooling_preview = "androidx.compose.ui:ui-tooling-preview"
    const val compose_tooling = "androidx.compose.ui:ui-tooling"

    //More Android Compose
    const val compose_activity = "androidx.activity:activity-compose:${LibsVersion.composeActivity}"
    const val compose_viewModel =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${LibsVersion.composeViewModel}"
    const val compose_iconExtended =
        "androidx.compose.material:material-icons-extended"

    const val compose_navigation = "androidx.navigation:navigation-compose"
    const val compose_navigation_animation = "com.google.accompanist:accompanist-navigation-animation"
    const val compose_navigation_test = "androidx.navigation:navigation-testing:2.5.3"
    const val compose_glide = "com.github.bumptech.glide:compose:1.0.0-alpha.1"


    // UI Tests
    const val compose_unitTest = "androidx.compose.ui:ui-test-junit4"
    const val compose_manifestTest = "androidx.compose.ui:ui-test-manifest"


}

object Debugging {
    const val flipperCore = "com.facebook.flipper:flipper:${LibsVersion.flipperVersion}"
    const val flipperNetwork =
        "com.facebook.flipper:flipper-network-plugin:${LibsVersion.flipperVersion}"
}

object InstrumentedTestLibraries {
    const val core = "androidx.test:core:${LibsVersion.testCore}"
    const val espresso = "androidx.test.espresso:espresso-core:${LibsVersion.espresso}"
    const val junit = "androidx.test.ext:junit:${LibsVersion.junitExt}"
}

object TestLibraries {
    const val junit = "junit:junit:${LibsVersion.junit}"
    const val kotlinCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${LibsVersion.kotlinCoroutines}"
    const val mockito = "org.mockito:mockito-core:${LibsVersion.mockito}"
    const val mockitoKotlin =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${LibsVersion.mockitoKotlin}"
    const val mockk = "io.mockk:mockk:${LibsVersion.mockk}"
    const val roboeletric = "org.robolectric:robolectric:${LibsVersion.robolectric}"
    const val faker = "io.github.serpro69:kotlin-faker:1.13.0"
}
