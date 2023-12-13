package com.chat.joycom

import androidx.lifecycle.viewModelScope
import com.chat.joycom.network.AppApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val appApiRepo: AppApiRepo) : BaseViewModel() {

}