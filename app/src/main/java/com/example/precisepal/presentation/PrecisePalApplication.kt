package com.example.precisepal.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//this setup is necessary for hilt to handle dependency injection throughout the entire application
@HiltAndroidApp
class PrecisePalApplication: Application()