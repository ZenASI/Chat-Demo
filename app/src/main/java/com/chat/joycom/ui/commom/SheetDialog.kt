package com.chat.joycom.ui.commom

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chat.joycom.R

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
                Button(onClick = { showState.invoke(false) }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                Button(
                    onClick = {
                        onText.invoke(inputText)
                        showState.invoke(false)
                    }
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    }
}