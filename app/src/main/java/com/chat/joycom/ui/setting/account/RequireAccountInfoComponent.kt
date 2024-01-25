package com.chat.joycom.ui.setting.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chat.joycom.R
import com.chat.joycom.ui.commom.IconTextH

@Composable
fun RequireAccountInfo(
    required: Boolean = false,
    clickAction: (() -> Unit)? = null,
    textTitle: String,
    textBefore: String,
    textAfter: String,
    remind: @Composable () -> Unit,
) {
    Text(
        text = textTitle,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    )
    Divider(thickness = 0.2.dp, color = MaterialTheme.colorScheme.secondary.copy(.3f))
    IconTextH(
        icon = {
            Icon(
                painterResource(id = R.drawable.ic_doc),
                "",
                modifier = Modifier.size(40.dp)
            )
        },
        text = {
            if (required) {
                Column {
                    Text(text = textAfter)
                    Text(
                        text = stringResource(id = R.string.require_account_sent_note),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                Text(text = textBefore)
            }
        },
        spaceWeightEnable = Pair(false, true),
        modifier = Modifier
            .clickable {
                clickAction?.invoke()
            }
            .padding(horizontal = 20.dp)
    )
    Divider(thickness = 0.2.dp, color = MaterialTheme.colorScheme.secondary.copy(.3f))
    if (required) {
        Text(
            text = stringResource(id = R.string.account_info_file_will_ready_three_day_later),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
    } else {
        remind.invoke()
    }
}
