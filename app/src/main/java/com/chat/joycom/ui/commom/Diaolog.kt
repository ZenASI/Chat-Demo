package com.chat.joycom.ui.commom

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val radioOptions =
        listOf(R.string.twenty_four_hour, R.string.seven_days, R.string.ninety_days, R.string.close)

    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(radioOptions[0]) }
    AlertDialog(
        onDismissRequest = { showState.invoke(false) },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.limited_time_msg),
                Modifier.padding(horizontal = 10.dp),
                fontSize = 20.sp
            )
            Text(
                text = stringResource(id = R.string.limited_time_msg_desc),
                Modifier.padding(horizontal = 10.dp)
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldSheet(
    name: String,
    @StringRes title: Int,
    textLimit: Int = 30,
    showState: (Boolean) -> Unit,
    onText: (String) -> Unit,
) {

    var inputText by rememberSaveable {
        mutableStateOf(name)
    }
    val backgroundColor = MaterialTheme.colorScheme.background

    AlertDialog(
        onDismissRequest = { showState.invoke(false) },
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = stringResource(id = title))
            Spacer(modifier = Modifier.size(10.dp))
            TextField(
                value = inputText,
                onValueChange = {
                    if (it.length <= textLimit) inputText = it
                },
                supportingText = {
                    Text(
                        text = "${inputText.length} / $textLimit",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showState.invoke(false) },
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Button(
                    onClick = {
                        onText.invoke(inputText)
                        showState.invoke(false)
                    },
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupIconSelectSheet(showState: (Boolean) -> Unit) {
    val textList = listOf(
        R.string.camera,
        R.string.gallery,
        R.string.emoji_and_sticker,
        R.string.on_internet_search
    )
    val iconList =
        listOf(R.drawable.ic_camera, R.drawable.ic_image, R.drawable.ic_emoji, R.drawable.ic_search)
    ModalBottomSheet(onDismissRequest = { showState.invoke(false) }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            Modifier.fillMaxWidth(), contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            textList.forEachIndexed { index, id ->
                item {
                    IconTextV(
                        icon = {
                            Icon(
                                painterResource(id = iconList[index]),
                                "",
                                modifier = Modifier.size(50.dp)
                            )
                        },
                        text = { Text(text = stringResource(id = id)) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            showState.invoke(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InfoCardDialog(
    onDismissRequest: (Boolean) -> Unit,
    title: String,
    imgUrl: String,
    callBack: () -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.background
    Dialog(
        onDismissRequest = { onDismissRequest.invoke(false) },
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxWidth(.8f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                SimpleUrlImage(
                    url = imgUrl ,
                    modifier = Modifier
                        .fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.ic_def_user),
                    error = painterResource(id = R.drawable.ic_def_user),
                    contentScale = ContentScale.FillWidth
                )
                Text(
                    text = title,
                    fontSize = 26.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray.copy(alpha = .5f)),
                    color = Color.White
                )
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(40.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(id = R.drawable.ic_chat),
                    "",
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                        .clickable {
                            callBack.invoke()
                        }
                )
                Icon(
                    painterResource(id = R.drawable.ic_phone),
                    "",
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                        .clickable {
                            callBack.invoke()
                        }
                )
                Icon(
                    painterResource(id = R.drawable.ic_videocam),
                    "",
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                        .clickable {
                            callBack.invoke()
                        }
                )
                Icon(
                    painterResource(id = R.drawable.ic_info),
                    "",
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                        .clickable {
                            callBack.invoke()
                        }
                )
            }
        }
    }
}