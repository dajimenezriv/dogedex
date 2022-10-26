package com.dajimenezriv.dogedex.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthField(
    labelId: Int,
    value: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessageId: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (errorMessageId != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(id = errorMessageId),
                color = Color.Red
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = labelId)) },
            value = value,
            onValueChange = { onTextChanged(it) },
            visualTransformation = visualTransformation,
            isError = errorMessageId != null
        )
    }
}