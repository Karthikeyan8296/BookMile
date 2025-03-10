// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //we are telling the gradle that it is an android application!
    alias(libs.plugins.android.application) apply false
    //Kotlin Android library
    alias(libs.plugins.kotlin.android) apply false
    //Kotlin Compose library
    alias(libs.plugins.kotlin.compose) apply false
    //kotlin serialization
    alias(libs.plugins.kotlinx.serialization) apply false
    //dagger hilt
    alias(libs.plugins.dagger.hilt) apply false
    //ksp
    alias(libs.plugins.ksp) apply false
    //firebase
    alias(libs.plugins.google.services) apply false

    //this plugins will be initialized in the libs.version file
}