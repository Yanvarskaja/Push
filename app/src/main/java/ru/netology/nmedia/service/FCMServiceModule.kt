package ru.netology.nmedia.service

import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FCMServiceModule {

    @Provides
    @Singleton
    fun provideFirebaseInstallations()  : FirebaseInstallations {
        return FirebaseInstallations.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseMessaging()  : FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}