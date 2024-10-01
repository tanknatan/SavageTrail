package com.Savage.Trail.presentation.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Color.Sphere.Challenge.gamecolor.data.Prefs
import com.Color.Sphere.Challenge.gamecolor.data.SoundManager
import com.Savage.Trail.R
import com.Savage.Trail.domain.Level
import com.Savage.Trail.presentation.navigatio.OutlinedText
import com.Savage.Trail.presentation.navigatio.Screen
import com.ancient.flow.game.presentation.view.LevelManager
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(
    level: Level ,
    onBack: () -> Unit ,
    restartGame: () -> Unit,
    onNextLevel: () -> Unit,
    onNext: (Screen) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LevelPreferences", Context.MODE_PRIVATE)
    val levelManager = LevelManager(sharedPreferences)
    var score by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(level.time) }
    var rocketPosition by remember { mutableFloatStateOf(0f) }
    val rocketSpeed = 5f
    var coins by remember { mutableStateOf(listOf<Coin>()) }
    var bombs by remember { mutableStateOf(listOf<Bomb>()) }  // List of bombs
    var gameOver by remember { mutableStateOf(false) }
    var levelCompleted by remember { mutableStateOf(false) }
    var isMovingLeft by remember { mutableStateOf(false) }
    var isMovingRight by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        } else {
            gameOver = true
        }
    }

    if (!gameOver && !levelCompleted) {
        // Game UI
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.background),
                    contentScale = ContentScale.FillBounds
                ),
            contentAlignment = Alignment.Center
        ) {
            val maxHeight = constraints.maxHeight
            val screenWidth = constraints.maxWidth * 0.3f

            val leftLimit = -screenWidth / 2 + 40f // Set left limit, taking into account rocket size
            val rightLimit = screenWidth / 2 - 40f // Set right limit

            LaunchedEffect(isMovingLeft, isMovingRight) {
                while (isMovingLeft || isMovingRight) {
                    delay(16L) // Frame delay for roughly 60 FPS

                    if (isMovingLeft) {
                        if (rocketPosition > leftLimit) {
                            rocketPosition -= rocketSpeed
                        } else {
                            rocketPosition = leftLimit // Prevent rocket from going past the left limit
                            isMovingLeft = false
                        }
                    }

                    if (isMovingRight) {
                        if (rocketPosition < rightLimit) {
                            rocketPosition += rocketSpeed
                        } else {
                            rocketPosition = rightLimit // Prevent rocket from going past the right limit
                            isMovingRight = false
                        }
                    }
                }
            }
            // Display coins and bombs
            coins.forEach { coin -> CoinItem(coin.x, coin.y) }
            bombs.forEach { bomb -> BombItem(bomb.x, bomb.y) }

            // Display score
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 16.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Top
//            ) {
                Image(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 16.dp)
                        .clickable(

                            onClick = {
                                SoundManager.playSound()
                                onBack()
                            }
                        )
                )

                OutlinedText(
                    text = "$score / ${level.targetScore}",
                    outlineColor = Color.Black,
                    fillColor = Color.White,
                    fontSize = 35.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.settings),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .clickable(

                            onClick = {
                                SoundManager.playSound()
                                onNext(Screen.OptionScreen)
                            }
                        )
                )


         //   }



            // Display time left
            OutlinedText(
                text = "Time: ${timeLeft}s",
                fontSize = 24.sp,
                fillColor = Color.White,
                outlineColor = Color.Black,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )

            // Display rocket
            Box(
                modifier = Modifier
                    .offset(x = rocketPosition.dp, y = (maxHeight * 0.1f).dp)
                    .size(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tiger),
                    contentDescription = "Rocket",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Movement buttons (left and right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { rocketPosition -= rocketSpeed },
                    modifier = Modifier
                        .pointerInteropFilter {
                            when (it.action) {
                                android.view.MotionEvent.ACTION_DOWN -> isMovingLeft = true
                                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> isMovingLeft = false
                            }
                            true
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = { rocketPosition += rocketSpeed },
                    modifier = Modifier
                        .pointerInteropFilter {
                            when (it.action) {
                                android.view.MotionEvent.ACTION_DOWN -> isMovingRight = true
                                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> isMovingRight = false
                            }
                            true
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Generate coins and bombs
            LaunchedEffect(Unit) {
                val (newCoins, newBombs) = generateCoinsAndBombs(level, leftLimit,rightLimit,screenWidth ,maxHeight)
                coins += newCoins
                bombs += newBombs
                while (timeLeft > 0) {
                    delay(10000L)
                    val (newCoins, newBombs) = generateCoinsAndBombs(level, leftLimit,rightLimit,screenWidth ,maxHeight)
                    coins += newCoins
                    bombs += newBombs
                }
            }

            // Smooth movement and collision detection
            LaunchedEffect(Unit) {
                while (timeLeft > 0) {
                    delay(16L)

                    // Move coins and bombs down smoothly
                    coins = coins.map { coin ->
                        val newCoin = coin.copy(y = coin.y + 4)
                        if (newCoin.y > maxHeight) {
                            Coin(
                                (-screenWidth.roundToInt()..screenWidth.roundToInt()).random()
                                    .toFloat(),
                                Random.nextFloat() * -maxHeight - 50f
                            )
                        } else {
                            newCoin
                        }
                    }

                    bombs = bombs.map { bomb ->
                        val newBomb = bomb.copy(y = bomb.y + 4)
                        if (newBomb.y > maxHeight) {
                            Bomb(
                                (-screenWidth.roundToInt()..screenWidth.roundToInt()).random()
                                    .toFloat(),
                                Random.nextFloat() * -maxHeight - 50f
                            )
                        } else {
                            newBomb
                        }
                    }

                    // Collision detection for coins
                    coins = coins.filter { coin ->
                        val coinCollected = coin.y > (maxHeight * 0.1f) && coin.y < (maxHeight * 0.1f + 80) &&
                                abs(coin.x - rocketPosition) < 50
                        if (coinCollected) {
                            score += 100
                        }
                        !coinCollected
                    }

                    // Collision detection for bombs
                    bombs.forEach { bomb ->
                        val bombHit = bomb.y > (maxHeight * 0.1f) && bomb.y < (maxHeight * 0.1f + 80) &&
                                abs(bomb.x - rocketPosition) < 50
                        if (bombHit) {
                            gameOver = true // End the game if player hits a bomb
                        }
                    }

                    // Check if player won the level
                    if (score >= level.targetScore) {
                        levelCompleted = true
                        levelManager.unlockNextLevel(level.ordinal + 1)
                    }
                }
            }
        }
    } else if (levelCompleted) {
        WinScreen(score, onNextLevel,level,onBack)
    } else {
        LossScreen( restartGame,level,onBack)
    }
}

// Coin data class to store position
data class Coin(val x: Float, val y: Float)

// Bomb data class to store position
data class Bomb(val x: Float, val y: Float)

// Coin and Bomb composables for drawing
@Composable
fun CoinItem(x: Float, y: Float) {
    Image(
        modifier = Modifier
            .offset(x = x.dp, y = y.dp)
            .size(40.dp),
        painter = painterResource(id = R.drawable.coin),
        contentDescription = "Coin",
        contentScale = ContentScale.Fit
    )
}

@Composable
fun BombItem(x: Float, y: Float) {
    Image(
        modifier = Modifier
            .offset(x = x.dp, y = y.dp)
            .size(40.dp),
        painter = painterResource(id = R.drawable.bomb),  // Replace with your bomb drawable
        contentDescription = "Bomb",
        contentScale = ContentScale.Fit
    )
}

// Function to generate initial set of coins and bombs based on the level
fun generateCoinsAndBombs(level: Level, left:Float, right:Float, screenWidth: Float, height: Int): Pair<List<Coin>, List<Bomb>> {
    val numberOfCoins = 5 + level.number // Increase the number of coins per level
    val numberOfBombs = 2+ level.number // Increase the number of bombs per level
    val coins = List(numberOfCoins) {
        // Spawn coins at the top, with a y-coordinate above the screen, and x-position randomly
        Coin(
            (left.roundToInt()..right.roundToInt()).random().toFloat(),
            Random.nextFloat() * -height - 50f
        ) // Coins spawn off-screen above the visible area
    }
    val bombs = List(numberOfBombs) {
        // Spawn bombs similarly to coins
        Bomb(
            (-screenWidth.roundToInt()..screenWidth.roundToInt()).random().toFloat(),
            Random.nextFloat() * -height - 50f
        )
    }
    return Pair(coins, bombs)
}


@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun LossScreen(next: () -> Unit,level: Level,onBack : () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background), // Background image
                contentScale = ContentScale.FillBounds
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedText(
            text = "DEFEAT",
            outlineColor = Color(0xFFBD4242),
            fillColor = Color.White,
            fontSize = 50.sp
        )

        // Score Display Box
        Box(
            modifier = Modifier
                .size(300.dp)
                .paint(
                    painter = painterResource(id = R.drawable.rec_act), // Button background
                    contentScale = ContentScale.Crop
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedText(
                text = "Level ${level.number}\n\nBomb catched",
                outlineColor = Color(0xFFBD4242),
                fillColor = Color.White,
                fontSize = 35.sp
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        // Menu Button
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
                        next()
                    }
            )
            OutlinedText(
                text = "REPEAT",
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
                text = "HOME",
                outlineColor = Color.Black,
                fillColor = Color.White,
                fontSize = 50.sp
            )
        }
    }
}

@Composable
private fun WinScreen(score: Int, next: () -> Unit,level: Level,onBack : () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.background), // Background image
                contentScale = ContentScale.FillBounds
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedText(
            text = "GREAT!",
            outlineColor = Color(0xFFBD4242),
            fillColor = Color.White,
            fontSize = 50.sp
        )

        // Score Display Box
        Box(
            modifier = Modifier
                .size(300.dp)
                .paint(
                    painter = painterResource(id = R.drawable.rec_act), // Button background
                    contentScale = ContentScale.Crop
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            OutlinedText(
                text = "Level ${level.number}\n\n${score}/ ${level.targetScore}",
                outlineColor = Color(0xFFBD4242),
                fillColor = Color.White,
                fontSize = 35.sp
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        // Menu Button
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
                        next()
                    }
            )
            OutlinedText(
                text = "NEXT",
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
                text = "HOME",
                outlineColor = Color.Black,
                fillColor = Color.White,
                fontSize = 50.sp
            )
        }
    }
}