package com.chat.joycom.ui.login

import androidx.lifecycle.viewModelScope
import com.chat.joycom.BaseViewModel
import com.chat.joycom.flow.AccountFlow
import com.chat.joycom.network.ApiResult
import com.chat.joycom.network.AppApiRepo
import com.chat.joycom.network.UrlPath
import com.chat.joycom.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class LoginViewModel @Inject constructor(private val appApiRepo: AppApiRepo) : BaseViewModel() {
    fun sendSms(countryCode: String, phone: String) {
        if (phone.isEmpty()) return
        viewModelScope.launch {
            flow { emit(appApiRepo.getBasicConfig()) }
                .flatMapConcat {
                    when (it) {
                        is ApiResult.OnSuccess -> {
                            UrlPath.config = it.data // set to object
                            flow { emit(appApiRepo.sendSms(countryCode.substring(1), phone)) }
                        }

                        is ApiResult.OnFail -> {
                            Timber.d("getBasicConfig error ${it.message}")
                            flow { emit(null) }
                        }
                    }
                }
                .collect {
                    when (it) {
                        is ApiResult.OnSuccess -> {

                        }

                        is ApiResult.OnFail -> {
                            Timber.d("sendSms error ${it.message}")
                        }

                        else -> {}
                    }
                }
        }
    }

    fun goLogin(countryCode: String, phone: String, otp: String) {
        if (countryCode.isEmpty() or phone.isEmpty() or otp.isEmpty()) return
        viewModelScope.launch {
            flow { emit(appApiRepo.getBasicConfig()) }
                .flatMapConcat {
                    if (it is ApiResult.OnSuccess) {
                        UrlPath.config = it.data
                    }
                    flow { emit(appApiRepo.goPhoneLogin(countryCode.substring(1), phone, otp)) }
                }
                .collect {
                    when (it) {
                        is ApiResult.OnSuccess -> {
                            AccountFlow.updateValue(it.data)
                            sendState(UiEvent.LoginSuccessEvent(it.data))
                        }

                        is ApiResult.OnFail -> {
                            Timber.d("goLogin ${it.message}")
                        }
                    }
                }
        }
    }

    fun goRegister(nickName: String, countryCode: String, phone: String, otp: String) {
        if (phone.isEmpty() or otp.isEmpty() or nickName.isEmpty()) return
        viewModelScope.launch {
            flow {
                emit(appApiRepo.getBasicConfig())
            }.flatMapConcat {
                if (it is ApiResult.OnSuccess) {
                    UrlPath.config = it.data
                }
                flow { emit(appApiRepo.goRegister(nickName, countryCode.substring(1), phone, otp)) }
            }.collect{
                when(it){
                    is ApiResult.OnSuccess -> {

                    }
                    is ApiResult.OnFail -> {
                        Timber.d("goRegister error => ${it.message}")
                    }
                }
            }
        }
    }
}