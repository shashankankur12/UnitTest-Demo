package com.example.unittestdemoproject.example9

import android.app.Application
import com.example.unittestdemoproject.example9.data.network.ApiInterFace
import com.example.unittestdemoproject.example9.data.network.SharedPref
import com.example.unittestdemoproject.example9.data.reopsitories.UserRepository
import com.example.unittestdemoproject.example9.ui.login.LoginViewModelFactory
import com.example.unittestdemoproject.example9.utils.NetworkConnectionInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class MyApplication :Application(), KodeinAware {
    override val kodein: Kodein= Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterFace(instance()) }
        bind() from singleton { SharedPref(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { LoginViewModelFactory(instance(), instance()) }

    }


}