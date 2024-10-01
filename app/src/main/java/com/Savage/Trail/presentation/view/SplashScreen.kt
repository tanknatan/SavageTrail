package com.ancient.flow.game.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Savage.Trail.R
import com.Savage.Trail.presentation.navigatio.OutlinedText
import com.Savage.Trail.presentation.navigatio.Screen


import kotlinx.coroutines.delay


@Composable
fun SplashScreen(  onNext: (Screen) -> Unit) {

    LaunchedEffect(Unit) {
        delay(1000) // Short delay before transitioning
        onNext(Screen.MainMenuScreen)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop, // To make the image fill the entire screen
            modifier = Modifier.fillMaxSize()
        )

        // Column removed from center and placed at bottom center
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Add padding if necessary
            contentAlignment = Alignment.Center // Changed to BottomCenter
        ) {
            OutlinedText(
                text = "Loading...",
                outlineColor = Color(0xFFBD4242),
                fillColor = Color.White,
                fontSize = 50.sp,
            )
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen {}
}


