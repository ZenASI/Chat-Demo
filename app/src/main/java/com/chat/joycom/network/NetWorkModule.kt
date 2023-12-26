package com.chat.joycom.network

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {

    @Singleton
    @Provides
    fun provideHttpLog(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        }).setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideMoshiConvert(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideApiServices(retrofit: Retrofit): AppApiService =
        retrofit.create(AppApiService::class.java)

    @Singleton
    @SocketOkhttp
    @Provides
    fun provideSocketOkhttp(netCookieJar: NetCookieJar): OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(netCookieJar)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .pingInterval(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @ApiOkhttp
    @Provides
    fun provideApiOkHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        netCookieJar: NetCookieJar,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .cookieJar(netCookieJar)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        @ApiOkhttp okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
//        .baseUrl("https://c1.joycom.vip")
        .baseUrl("https://99.83.189.18")
        .addConverterFactory(moshiConverterFactory)
        .client(okHttpClient)
        .build()
}