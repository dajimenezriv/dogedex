package com.dajimenezriv.dogedex.doglist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.dajimenezriv.dogedex.R
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.composables.BackNavigationIcon
import com.dajimenezriv.dogedex.composables.ErrorDialog
import com.dajimenezriv.dogedex.composables.LoadingWheel
import com.dajimenezriv.dogedex.models.Dog

private const val GRID_COLS_COUNT = 3

@Composable
fun DogListScreen(
    onNavigationIconClick: () -> Unit,
    onDogClicked: (Dog) -> Unit,
    // here, hilt goes to DogListViewModel and injects the viewModel using the provided constructor there
    viewModel: DogListViewModel = hiltViewModel()
) {
    val status = viewModel.status.value
    val dogs = viewModel.dogs.value

    Scaffold(
        topBar = { DogListTopBar(onNavigationIconClick) },
        content = { padding ->
            LazyVerticalGrid(
                modifier = Modifier.padding(padding),
                columns = GridCells.Fixed(GRID_COLS_COUNT), content = {
                    items(dogs) {
                        DogGridItem(dog = it, onDogClicked = onDogClicked)
                    }
                })
        }
    )

    if (status is APIResponseStatus.Loading) LoadingWheel()
    else if (status is APIResponseStatus.Error) {
        ErrorDialog(
            messageId = status.messageId,
            onDialogDismiss = { viewModel.resetAPIResponseStatus() }
        )
    }
}

@Composable
fun DogListTopBar(onClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.top_bar_title)) },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        navigationIcon = { BackNavigationIcon(onClick = onClick) }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DogGridItem(dog: Dog, onDogClicked: (Dog) -> Unit) {
    if (dog.inCollection) {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            onClick = { onDogClicked(dog) },
            shape = RoundedCornerShape(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = dog.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.White)
                    .semantics { testTag = "dog-${dog.name}" },
            )
        }
    } else {
        Surface(
            modifier = Modifier
                .padding(8.dp)
                .height(100.dp)
                .width(100.dp),
            color = Color.Red,
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                text = dog.index.toString()
            )
        }
    }
}
