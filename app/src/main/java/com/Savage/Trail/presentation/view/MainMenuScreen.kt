package com.ancient.flow.game.presentation.view

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Color.Sphere.Challenge.gamecolor.data.SoundManager
import com.Savage.Trail.R
import com.Savage.Trail.presentation.navigatio.OutlinedText
import com.Savage.Trail.presentation.navigatio.Screen


@Composable
fun MainMenuScreen(
    onNext: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.background), // Замените на ваше фоновое изображение
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedText(
                text = "MENU",
                outlineColor = Color(0xFFBD4242),
                fillColor = Color.White,
                fontSize = 50.sp
            )
            Spacer(modifier = Modifier.height(80.dp))

            // Кнопка "Start"
            Box(
                modifier = Modifier

                    .padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rec), // Замените на ваш ресурс для кнопки
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier

                        .fillMaxWidth(0.7f)
                        .clickable {
                            SoundManager.playSound()
                            onNext(Screen.LevelScreen)
                        }
                )
                OutlinedText(
                    text = "PLAY",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 50.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            // Кнопка "Options"
            Box(
                modifier = Modifier

                    .padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rec), // Замените на ваш ресурс для кнопки
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier

                        .fillMaxWidth(0.7f)

                        .clickable {
                            SoundManager.playSound()
                            onNext(Screen.OptionScreen)
                        }
                )
                OutlinedText(
                    text = "OPTIONS",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 50.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))

            // Кнопка "Policy"

            Box(
                modifier = Modifier
                    .padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rec), // Замените на ваш ресурс для кнопки
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier

                        .fillMaxWidth(0.7f)

                        .clickable {
                            SoundManager.playSound()
                            onNext(Screen.ExitScreen)
                        }
                )
                OutlinedText(
                    text = "EXIT",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 50.sp
                )
            }
        }
    }
}