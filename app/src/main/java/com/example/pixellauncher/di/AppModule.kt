package com.example.pixellauncher.di

import android.content.Context
import com.example.pixellauncher.data.preferences.LauncherPreferences
import com.example.pixellauncher.data.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        @ApplicationContext context: Context
    ): AppRepository = AppRepository(context)

    @Provides
    @Singleton
    fun provideLauncherPreferences(
        @ApplicationContext context: Context
    ): LauncherPreferences = LauncherPreferences(context)
}
