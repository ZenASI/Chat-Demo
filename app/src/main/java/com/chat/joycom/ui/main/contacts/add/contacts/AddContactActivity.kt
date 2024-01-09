package com.chat.joycom.ui.main.contacts.add.contacts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.BaseActivity
import com.chat.joycom.ui.commom.JoyComAppBar
import com.chat.joycom.ui.theme.JoyComTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AddContactActivity : BaseActivity() {

    companion object {
        fun start(
            context: Context,
        ) {
            context.startActivity(
                Intent(context, AddContactActivity::class.java)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoyComTheme {
                Surface {
                    Scaffold(topBar = {
                        JoyComAppBar(
                            title = { Text(text = stringResource(id = R.string.add_new_contact)) }
                        )
                    }) { paddingValues ->
                        var lastName by remember {
                            mutableStateOf("")
                        }
                        var firstName by remember {
                            mutableStateOf("")
                        }
                        var phone by remember {
                            mutableStateOf("")
                        }
                        var code by remember {
                            mutableStateOf("")
                        }
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxWidth(1f),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(.9f)
                            ) {
                                Image(
                                    painterResource(id = R.drawable.ic_def_user),
                                    "",
                                    modifier = Modifier.size(40.dp)
                                )
                                TextField(
                                    value = lastName,
                                    onValueChange = { lastName = it },
                                    label = { Text(text = stringResource(id = R.string.last_name)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth(.9f)
                            ) {
                                Spacer(modifier = Modifier.size(40.dp))
                                TextField(
                                    value = firstName,
                                    onValueChange = { firstName = it },
                                    label = { Text(text = stringResource(id = R.string.first_name)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(.9f),
                            ) {
                                Image(Icons.Filled.Phone, "", modifier = Modifier.size(40.dp))
                                TextField(
                                    value = code,
                                    onValueChange = { code = it },
                                    label = { Text(text = stringResource(id = R.string.country)) },
                                    readOnly = true,
                                    interactionSource = remember { MutableInteractionSource() }
                                        .also { interactionSource ->
                                            LaunchedEffect(interactionSource) {
                                                interactionSource.interactions.collect {
                                                    if (it is PressInteraction.Release) {
                                                        // works like onClick
                                                        Timber.d("Click Intent")
                                                    }
                                                }
                                            }
                                        },
                                    modifier = Modifier
                                        .weight(.35f)
                                )
                                TextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text(text = stringResource(id = R.string.phone)) },
                                    modifier = Modifier.weight(.65f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                                )
                            }
                            Row {

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { finish() },
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                            ) {
                                Text(text = stringResource(id = R.string.save))
                            }
                        }
                    }
                }
            }
        }
    }
}