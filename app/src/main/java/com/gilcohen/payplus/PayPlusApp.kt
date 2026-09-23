package com.gilcohen.payplus

import android.app.Application
import com.gilcohen.payplus.di.AppContainer

class PayPlusApp : Application() {
    val container: AppContainer by lazy { AppContainer() }
}
