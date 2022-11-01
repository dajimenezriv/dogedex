package com.dajimenezriv.dogedex.dogdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.models.Dog

@Composable
fun ProbableDogsDialog(
    probableDogs: MutableList<Dog>,
    onDismiss: () -> Unit,
    onItemClicked: (Dog) -> Unit,
) {
    AlertDialog(
        // when we click outside the dialog
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(id = R.string.other_probable_dogs),
                color = colorResource(id = R.color.text_black),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        },
        text = {
            ProbableDogsList(dogs = probableDogs) {
                onItemClicked(it)
                onDismiss()
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        }
    )
}

@Composable
fun ProbableDogsList(dogs: MutableList<Dog>, onItemClicked: (Dog) -> Unit) {
    Box(modifier = Modifier.height(250.dp)) {
        LazyColumn(content = {
            items(dogs) {
                ProbableDogItem(dog = it, onItemClicked)
            }
        })
    }
}

@Composable
fun ProbableDogItem(dog: Dog, onItemClicked: (Dog) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                enabled = true,
                onClick = { onItemClicked(dog) }
            )
    ) {
        Text(
            text = dog.name,
            modifier = Modifier.padding(8.dp),
            color = colorResource(id = R.color.text_black)
        )
    }
}
