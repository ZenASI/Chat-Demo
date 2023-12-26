package com.chat.joycom.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.main.MainActivity
import com.chat.joycom.ui.UiEvent
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    companion object{
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, LoginActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        initCollect()

        setContent {
            JoyComTheme {
                Surface {
                    val loginScenesList = LoginScene.values().toList()
                    val pagerState = rememberPagerState { loginScenesList.size }
                    val scope = rememberCoroutineScope()
                    Scaffold(
                        topBar = {
                            TabRow(selectedTabIndex = pagerState.currentPage) {
                                loginScenesList.forEachIndexed { index, item ->
                                    Tab(
                                        selected = index == pagerState.currentPage,
                                        onClick = {
                                            scope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }) {
                                        Text(
                                            text = item.sceneName,
                                            modifier = Modifier.padding(15.dp)
                                        )
                                    }
                                }
                            }
                        }
                    ) { paddingValues ->
                        HorizontalPager(
                            contentPadding = paddingValues,
                            state = pagerState
                        ) { pos ->
                            loginScenesList[pos].body.invoke()
                        }
                    }
                }
            }
        }
    }

    private fun initCollect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiEvent.SendSmsSuccessEvent -> {

                        }

                        is UiEvent.LoginSuccessEvent -> {
                            MainActivity.start(this@LoginActivity)
                            finish()
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}