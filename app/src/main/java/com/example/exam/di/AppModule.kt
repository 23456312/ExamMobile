// File: di/AppModule.kt
package com.example.exam.di

import android.content.Context
import android.content.SharedPreferences
import com.example.exam.data.local.SudokuLocalDataSource
import com.example.exam.data.remote.api.ApiConstants
import com.example.exam.data.remote.api.SudokuApiService
import com.example.exam.data.repository.SudokuRepositoryImpl
import com.example.exam.domain.repository.SudokuRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(ApiConstants.HEADER_API_KEY, ApiConstants.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideSudokuApiService(okHttpClient: OkHttpClient, gson: Gson): SudokuApiService {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(SudokuApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("sudoku_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        sharedPreferences: SharedPreferences,
        gson: Gson
    ): SudokuLocalDataSource {
        return SudokuLocalDataSource(sharedPreferences, gson)
    }

    @Provides
    @Singleton
    fun provideSudokuRepository(
        apiService: SudokuApiService,
        localDataSource: SudokuLocalDataSource
    ): SudokuRepository {
        return SudokuRepositoryImpl(apiService, localDataSource)
    }
}