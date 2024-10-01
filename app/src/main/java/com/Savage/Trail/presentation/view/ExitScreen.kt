package com.Savage.Trail.presentation.view

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Color.Sphere.Challenge.gamecolor.data.SoundManager
import com.Savage.Trail.R
import com.Savage.Trail.presentation.navigatio.OutlinedText
import com.Savage.Trail.presentation.navigatio.Screen


@Composable
fun ExitScreen(
    onBack: () -> Unit
) {
    val activity = LocalContext.current as? Activity
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "EXIT?",
                fontSize = 48.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 2.dp)
            )

            Box(
                modifier = Modifier

                    .padding(vertical = 0.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.norec), // Замените на ваш ресурс для кнопки
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier

                        .fillMaxWidth(0.7f)
                        .clickable {
                            SoundManager.playSound()
                            activity?.finish()
                        }
                )
                OutlinedText(
                    text = "YES",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 50.sp
                )
            }

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
                            onBack()
                        }
                )
                OutlinedText(
                    text = "NO",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 50.sp
                )
            }
        }
    }
}
