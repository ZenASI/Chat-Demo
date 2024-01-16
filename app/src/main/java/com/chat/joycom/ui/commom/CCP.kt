package com.chat.joycom.ui.commom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.chat.joycom.R
import com.chat.joycom.ui.theme.JoyComTheme
import com.hbb20.CountryCodePicker

@Composable
fun CCP(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val ccp = remember {
        CountryCodePicker(context).apply {
            setDefaultCountryUsingNameCode("US")
            setCountryPreference("US")
            setAutoDetectedCountry(true)
            setCountryAutoDetectionPref(CountryCodePicker.AutoDetectionPref.LOCALE_ONLY)
            setNumberAutoFormattingEnabled(true)
            setDialogKeyboardAutoPopup(false)
            showFlag(false)
            showArrow(false)
        }
    }
    JoyComTheme {
        Column(modifier = modifier.wrapContentSize()) {
            Text(text = stringResource(id = R.string.country))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AndroidView(
                    factory = { context -> ccp },
                    update = { ccp ->
                        ccp.apply {
                            setTextSize(50)
                            languageToApply
                        }
                    }
                )
                Icon(painterResource(id = R.drawable.ic_arrow_down), "", modifier = Modifier.clickable {
                    ccp.launchCountrySelectionDialog()
                })
            }
        }

    }
}