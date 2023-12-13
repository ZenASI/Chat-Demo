package com.chat.joycom.ui.commom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.utkuglsvn.countrycodepicker.libData.CountryCode
import com.utkuglsvn.countrycodepicker.libData.utils.getFlagMasterResID
import com.utkuglsvn.countrycodepicker.libData.utils.getLibCountries
import com.utkuglsvn.countrycodepicker.libUi.CountryCodePicker
import com.utkuglsvn.countrycodepicker.libUtils.searchCountryList

@Composable
fun PhoneInput(
    inputText: String,
    modifier: Modifier = Modifier,
    defaultCountry: CountryCode = getLibCountries().single { it.countryCode == "cn" },
    updateText: (text: String) -> Unit,
    pickCountry: (cc: CountryCode) -> Unit
) {
    var isOpenDialog by remember { mutableStateOf(false) }
    val countryList: List<CountryCode> = getLibCountries()
    var searchValue by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .clickable { isOpenDialog = true }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(id = getFlagMasterResID(defaultCountry.countryCode)), "")
            Spacer(modifier = Modifier.padding(3.dp))
            Text(text = defaultCountry.countryPhoneCode)
            Spacer(modifier = Modifier.padding(3.dp))
            Text(text = defaultCountry.countryCode)
        }
        TextField(
            value = inputText,
            onValueChange = {
                updateText(it)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text(text = "phone", color = Color.Gray) }
        )
        if (isOpenDialog) {
            Dialog(
                onDismissRequest = { isOpenDialog = false },
            ) {
                Card(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                        .background(MaterialTheme.colorScheme.background),
                    shape = RoundedCornerShape(12.dp),

                    ) {
                    Column {

                        searchValue = DialogSearchView(
                            textColor = Color.Black,
                            hintColor = Color.Gray,
                            textSelectColor = Color(0xff3898f0),
                        )

                        LazyColumn {
                            items(
                                (if (searchValue.isEmpty()) {
                                    countryList
                                } else {
                                    countryList.searchCountryList(searchValue)
                                })
                            ) { countryItem ->
                                Row(
                                    Modifier
                                        .padding(
                                            horizontal = 18.dp,
                                            vertical = 18.dp
                                        )
                                        .clickable {
                                            pickCountry(countryItem)
                                            isOpenDialog = false
                                        }) {
                                    Image(
                                        painter = painterResource(
                                            id = getFlagMasterResID(
                                                countryItem.countryCode
                                            )
                                        ), contentDescription = null,
                                        Modifier.size(width = 36.dp, height = 22.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        countryItem.countryName,
                                        Modifier.padding(horizontal = 18.dp),
                                        color = Color.Black,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogSearchView(
    textColor: Color,
    hintColor: Color,
    textSelectColor: Color,
): String {
    var searchValue by remember { mutableStateOf("") }
    Row {
        CustomTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            value = searchValue,
            onValueChange = {
                searchValue = it
            },
            fontSize = 14.sp,
            hint = "Search ...",
            textColor = textColor,
            hintColor = hintColor,
            textSelectColor = textSelectColor,
            textAlign = TextAlign.Start,
        )
    }
    return searchValue
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    fontSize: TextUnit = 16.sp,
    textColor: Color,
    hintColor: Color,
    textSelectColor: Color,
    textAlign: TextAlign = TextAlign.Center
) {
    Box(modifier) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = textSelectColor,
                backgroundColor = textSelectColor.copy(0.2f),
            )
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = textAlign,
                    fontSize = fontSize,
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = textColor.copy(0.2f)
                    )
                },
            )
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = hintColor,
                    modifier = Modifier.then(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 52.dp)
                    )
                )
            }
        }
    }
}