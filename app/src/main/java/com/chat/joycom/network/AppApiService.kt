package com.chat.joycom.network

import com.chat.joycom.model.BaseResponse
import com.chat.joycom.model.Config
import com.chat.joycom.model.Member
import com.chat.joycom.model.UserInfo
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface AppApiService {

    @GET("/mapi/user/getRegionConfig")
    suspend fun getConfig(): Response<BaseResponse<Config>>

    @FormUrlEncoded
    @POST
    suspend fun login(
        @Url url: String,
        @FieldMap data : Map<String, String>
    ): Response<BaseResponse<UserInfo>>

    @FormUrlEncoded
    @POST
    suspend fun sendSms(
        @Url url: String,
        @FieldMap data: Map<String, String>
    ): Response<BaseResponse<String>>

    @GET
    suspend fun queryMember(
        @Url url: String,
        @QueryMap data: Map<String, String>
    ): Response<BaseResponse<Member>>

    @FormUrlEncoded
    @POST
    suspend fun register(
        @Url url: String,
        @FieldMap data: Map<String, String>
    ): Response<BaseResponse<UserInfo>>

    @FormUrlEncoded
    @POST
    suspend fun logout(
        @Url url: String,
    ): Response<BaseResponse<String>>
}