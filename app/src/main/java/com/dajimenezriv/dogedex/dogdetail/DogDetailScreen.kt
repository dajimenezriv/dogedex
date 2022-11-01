package com.dajimenezriv.dogedex.dogdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import com.dajimenezriv.dogedex.composables.ErrorDialog
import com.dajimenezriv.dogedex.composables.LoadingWheel
import com.dajimenezriv.dogedex.models.Dog

@Composable
fun DogDetailScreen(
    finishActivity: () -> Unit,
    viewModel: DogDetailViewModel = hiltViewModel()
) {
    val probableDogsDialogEnabled = remember { mutableStateOf(false) }
    val status = viewModel.status.value
    val dog = viewModel.dog.value!!
    val isRecognition = viewModel.isRecognition.value ?: false
    // transform flow state to state
    val probableDogList = viewModel.probableDogList.collectAsState().value

    // when we add a dog to our collection (when we execute the bottom onClick function)
    // after the viewModel saves the dog, the viewModel status changes to Success
    if (status is APIResponseStatus.Success) finishActivity()

    // it`s like a frame layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.secondary_background))
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        DogInformation(dog = dog, isRecognition = isRecognition) {
            viewModel.getProbableDogs()
            probableDogsDialogEnabled.value = true
        }
        Image(
            modifier = Modifier
                .width(270.dp)
                .padding(top = 80.dp),
            painter = rememberAsyncImagePainter(dog.imageUrl),
            contentDescription = dog.name
        )
        FloatingActionButton(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 20.dp)
                .semantics { testTag = "closeDetailsScreen" },
            onClick = {
                if (isRecognition) viewModel.addDogToUser()
                else finishActivity()
            }
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
        }

        if (status is APIResponseStatus.Loading) LoadingWheel()
        else if (status is APIResponseStatus.Error) {
            ErrorDialog(
                messageId = status.messageId,
                onDialogDismiss = { viewModel.resetAPIResponseStatus() }
            )
        }

        if (probableDogsDialogEnabled.value) {
            ProbableDogsDialog(
                probableDogs = probableDogList,
                onDismiss = { probableDogsDialogEnabled.value = false },
                onItemClicked = { viewModel.updateDog(it) }
            )
        }
    }
}

@Composable
fun DogInformation(dog: Dog, isRecognition: Boolean, onProbableDogsButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 180.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            color = colorResource(id = android.R.color.white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(R.string.dog_index_format, dog.index),
                    fontSize = 32.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.End
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                    text = dog.name,
                    fontSize = 32.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                LiveIcon()

                Text(
                    text = stringResource(R.string.dog_life_expectancy_format, dog.lifeExpectancy),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = dog.temperament,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_black),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                if (isRecognition) {
                    Button(
                        modifier = Modifier.padding(16.dp),
                        onClick = { onProbableDogsButtonClick() }
                    ) {
                        Text(
                            text = stringResource(id = R.string.not_your_dog),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp
                        )
                    }
                }

                Divider(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 16.dp
                    ),
                    color = colorResource(id = R.color.divider),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DogDataColumn(
                        modifier = Modifier.weight(1f),
                        stringResource(id = R.string.female),
                        dog.weightFemale,
                        dog.heightFemale,
                    )

                    VerticalDivider()

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = dog.type,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.text_black),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp),
                            text = stringResource(id = R.string.group),
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.dark_gray),
                            textAlign = TextAlign.Center,
                        )
                    }

                    VerticalDivider()

                    DogDataColumn(
                        modifier = Modifier.weight(1f),
                        stringResource(id = R.string.male),
                        dog.weightMale,
                        dog.heightMale,
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .height(42.dp)
            .width(1.dp),
        color = colorResource(id = R.color.divider)
    )
}

@Composable
private fun DogDataColumn(
    modifier: Modifier = Modifier,
    genre: String,
    weight: String,
    height: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = genre,
            color = colorResource(id = R.color.text_black),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = weight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.text_black),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.weight),
            color = colorResource(id = R.color.dark_gray),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = height,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colorResource(id = R.color.text_black),
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(R.string.height),
            color = colorResource(id = R.color.dark_gray),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LiveIcon() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 80.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = colorResource(id = R.color.color_primary)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_hearth_white),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .padding(4.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(bottomEnd = 2.dp, topEnd = 2.dp),
            modifier = Modifier
                .width(200.dp)
                .height(6.dp),
            color = colorResource(id = R.color.color_primary)
        ) {

        }
    }
}
