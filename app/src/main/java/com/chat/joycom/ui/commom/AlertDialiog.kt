package com.chat.joycom.ui.commom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat.joycom.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDescAlert(
    type: PermissionType,
    showState: (Boolean) -> Unit,
    acceptCallback: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { showState.invoke(false) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val iconArray = when (type) {
                PermissionType.Camera -> intArrayOf(R.drawable.ic_camera)
                PermissionType.Contacts -> intArrayOf(R.drawable.ic_contacts)
                PermissionType.Storage -> intArrayOf(R.drawable.ic_file)
                PermissionType.StorageWithCamera -> intArrayOf(
                    R.drawable.ic_camera,
                    R.drawable.ic_file
                )

                PermissionType.Qrcode -> intArrayOf(R.drawable.ic_camera)
                PermissionType.RecordAudio -> intArrayOf(R.drawable.ic_mic)
            }
            val content = when (type) {
                PermissionType.Camera -> R.string.permission_camera_desc
                PermissionType.Contacts -> R.string.permission_contact_desc
                PermissionType.Storage -> R.string.permission_storage
                PermissionType.StorageWithCamera -> R.string.permission_camera_storage
                PermissionType.Qrcode -> R.string.permission_camera_qr_desc
                PermissionType.RecordAudio -> R.string.permission_record_audio_desc
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 40.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                iconArray.forEach {
                    Icon(painterResource(id = it), "", modifier = Modifier.size(50.dp))
                }
            }
            Text(text = stringResource(id = content))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        showState.invoke(false)
                    }
                ) {
                    Text(text = stringResource(id = R.string.not_now))
                }
                Button(
                    onClick = {
                        showState.invoke(false)
                        acceptCallback.invoke()
                    }
                ) {
                    Text(text = stringResource(id = R.string.keep_going))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitedTimeAlert(showState: (Boolean) -> Unit, selectCallback: (Int) -> Unit) {
    val radioOptions  =
        listOf(R.string.twenty_four_hour, R.string.seven_days, R.string.ninety_days, R.string.close)

    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(radioOptions[0] ) }
    AlertDialog(
        onDismissRequest = { showState.invoke(false) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
    ) {
        Column {
            Text(text = stringResource(id = R.string.limited_time_msg))
            Text(text = stringResource(id = R.string.limited_time_msg_desc))
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                selectCallback.invoke(text)
                            }
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            selectCallback.invoke(text)
                        }
                    )
                    Text(
                        text = stringResource(id = text),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}