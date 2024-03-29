package com.chat.joycom.network

import com.chat.joycom.model.BaseResponse
import com.chat.joycom.model.Config
import com.chat.joycom.model.GroupContact
import com.chat.joycom.model.Member
import com.chat.joycom.model.UserInfo
import com.chat.joycom.network.UrlPath.getFullUrlPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiRepo @Inject constructor(private val appApiService: AppApiService) {

    suspend fun queryGroupContacts(
        groupId: Long,
    ): ApiResult<List<GroupContact>> =
        withContext(Dispatchers.IO) {
            val groupContactMap = mapOf("GroupId" to groupId.toString())
            handleResponse { appApiService.groupContact(UrlPath.GROUP_CONTACT.getFullUrlPath(), groupContactMap) }
        }

    suspend fun logout(): ApiResult<String> = withContext(Dispatchers.IO) {
        handleResponse { appApiService.logout(UrlPath.LOGOUT.getFullUrlPath()) }
    }

    suspend fun queryMember(
        countryCode: String,
        phoneNumber: String,
    ): ApiResult<Member> = withContext(Dispatchers.IO) {
        val queryMemberMap =
            mapOf("CountryCode" to countryCode, "PhoneNumber" to phoneNumber)
        handleResponse {
            appApiService.queryMember(
                UrlPath.QUERY_MEMBER.getFullUrlPath(),
                queryMemberMap
            )
        }
    }

    suspend fun goRegister(
        nickName: String,
        countryCode: String,
        phoneNumber: String,
        otp: String
    ): ApiResult<UserInfo> = withContext(Dispatchers.IO) {
        val registerMap =
            mapOf(
                "Nickname" to nickName,
                "CountryCode" to countryCode,
                "PhoneNumber" to phoneNumber,
                "Captcha" to otp
            )
        handleResponse { appApiService.register(UrlPath.REGISTER.getFullUrlPath(), registerMap) }
    }

    suspend fun getBasicConfig(): ApiResult<Config> = withContext(Dispatchers.IO) {
        handleResponse { appApiService.getConfig() }
    }

    suspend fun goPhoneLogin(
        countryCode: String,
        phoneNumber: String,
        otp: String,
    ): ApiResult<UserInfo> =
        withContext(Dispatchers.IO) {
            val loginMap = mapOf(
                "CountryCode" to countryCode,
                "PhoneNumber" to phoneNumber,
                "Captcha" to otp
            )
            handleResponse { appApiService.login(UrlPath.LOGIN.getFullUrlPath(), loginMap) }
        }

    suspend fun sendSms(
        countryCode: String,
        phoneNumber: String,
    ): ApiResult<String> =
        withContext(Dispatchers.IO) {
            val smsMap =
                mapOf(
                    "CountryCode" to countryCode,
                    "PhoneNumber" to phoneNumber,
                )
            handleResponse { appApiService.sendSms(UrlPath.SEND_SMS.getFullUrlPath(), smsMap) }
        }

    private suspend fun <T> handleResponse(response: suspend () -> Response<BaseResponse<T>>): ApiResult<T> {
        return try {
            val result = response.invoke()
            if (result.isSuccessful) {
                // 200 ~ 299
                val body = result.body()
                if (body?.code == 200 && body.data != null) {
                    ApiResult.OnSuccess(body.data)
                } else {
                    ApiResult.OnFail(body?.code, body?.msg)
                }
            } else {
                // TODO: need to handle errorBody
                ApiResult.OnFail(result.code(), result.message())
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            if (e is HttpException) {
                ApiResult.OnFail(code = e.code(), message = e.message, e = e)
            } else {
                ApiResult.OnFail(code = null, message = e.message, e = e)
            }
        }
    }
}