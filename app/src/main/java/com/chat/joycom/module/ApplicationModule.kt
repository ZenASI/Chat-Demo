package com.chat.joycom.module

import android.content.Context
import com.chat.joycom.JoyComApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideApplicationInstance(@ApplicationContext context: Context) =
        context as JoyComApplication
}