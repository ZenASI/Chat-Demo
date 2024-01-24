package com.chat.joycom.ui.commom

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ref:https://github.com/JetBrains/compose-multiplatform/issues/202
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultInput(
    modifier: Modifier = Modifier,
    inputText: String,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
    onValueChange: (String) -> Unit,
    @StringRes hint: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    enableBottomLine: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = inputText,
        onValueChange = { onValueChange.invoke(it) },
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        singleLine = singleLine,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = inputText,
                innerTextField = innerTextField,
                placeholder = { if (hint != null) Text(text = stringResource(id = hint), fontSize = textStyle.fontSize) },
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (enableBottomLine) MaterialTheme.colorScheme.primary else Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultInput(
    modifier: Modifier = Modifier,
    inputText: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
    @StringRes hint: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    enableBottomLine: Boolean = false
){

    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = inputText,
        onValueChange = { onValueChange.invoke(it) },
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        singleLine = singleLine,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = inputText.text,
                innerTextField = innerTextField,
                placeholder = { if (hint != null) Text(text = stringResource(id = hint), fontSize = textStyle.fontSize) },
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = if (enableBottomLine) MaterialTheme.colorScheme.primary else Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    inputText: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
    @StringRes hint: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    prefixText: String? = null,
) {

    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = inputText,
        onValueChange = { onValueChange.invoke(it) },
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier,
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = @Composable { innerTextField ->
            // places leading icon, text field with label and placeholder, trailing icon
            TextFieldDefaults.DecorationBox(
                value = inputText.text,
                innerTextField = innerTextField,
                placeholder = {
                    if (hint != null) Text(
                        text = stringResource(id = hint),
                        fontSize = textStyle.fontSize
                    )
                },
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(0.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                prefix = { Text(text = prefixText ?: "") }
            )
        },
    )
}
