package com.Savage.Trail.presentation.navigatio

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.Color.Sphere.Challenge.gamecolor.data.Prefs
import com.Color.Sphere.Challenge.gamecolor.data.SoundManager
import com.Savage.Trail.domain.Level
import com.Savage.Trail.presentation.view.ExitScreen
import com.Savage.Trail.presentation.view.GameScreen
import com.Savage.Trail.ui.theme.SavageTrailTheme
import com.Savage.Trail.ui.theme.myfont
import com.ancient.flow.game.presentation.view.LevelScreen
import com.ancient.flow.game.presentation.view.MainMenuScreen
import com.ancient.flow.game.presentation.view.OptionsScreen
import com.ancient.flow.game.presentation.view.SplashScreen

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        Prefs.init(applicationContext)
        SoundManager.init(applicationContext)
        SoundManager.playMusic()
        setContent {
            SavageTrailTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {
                    composable(Screen.SplashScreen.route) {
                        SplashScreen(navController::navigatePopUpInclusive)
                    }
                    composable(Screen.MainMenuScreen.route) {
                        MainMenuScreen(navController::navigateSingleTop)
                    }
                    composable(Screen.LevelScreen.route) {
                        LevelScreen(
                            onBack = navController::popBackStack,
                            onChooseLevel = { level ->
                                navController.navigateSingleTop(Screen.GameScreen, level)
                            }

                        )
                    }
                    composable(Screen.OptionScreen.route) {
                        OptionsScreen(onBack = navController::popBackStack)
                    }
                    composable(Screen.ExitScreen.route) {
                        ExitScreen (
                            onBack = navController::popBackStack
                        )
                    }
                    composable(Screen.GameScreen.route) { backStackEntry ->
                        val level = Screen.GameScreen.getLevel(backStackEntry)
                        val levels = Level.entries.toTypedArray()

                        GameScreen(
                            level,
                            onBack = navController::popBackStack,
                            onNextLevel = {
                                navController.navigate("game_screen/${levels[level.ordinal+ 1].name}") {
                                    // Pop the back stack to clear the previous instance of the game screen
                                    popUpTo("game_screen/${levels[level.ordinal+ 1].name}") { inclusive = true }
                                }
                            },
                            restartGame = {
                                navController.navigate("game_screen/${level.name}") {
                                    // Pop the back stack to clear the previous instance of the game screen
                                    popUpTo("game_screen/${level.name}") { inclusive = true }
                                }
                            },
                            onNext =  navController::navigateSingleTop

                        )
                    }


                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        SoundManager.resumeSound()
    }

    override fun onPause() {
        super.onPause()
        SoundManager.pauseSound()
        SoundManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundManager.onDestroy()
    }
}


//@Composable
//fun CoinCatchGame() {
//    var basketPosition by remember { mutableStateOf(150f) }
//    var coins by remember { mutableStateOf(mutableListOf<Coin>()) }
//    var score by remember { mutableStateOf(0) }
//
//    // Метод для генерации монет
//    fun generateCoins() {
//        if (Random.nextInt(100) < 10) { // 10% вероятность генерации новой монеты
//            coins.add(Coin(Random.nextFloat() * 300, 0f)) // Генерация монеты с случайной x-координатой
//        }
//    }
//
//    // Метод для проверки совпадения координат
//    fun checkCollision(coin: Coin): Boolean {
//        return coin.y >= 500f && coin.x in (basketPosition - 50)..(basketPosition + 50)
//    }
//
//    // Основной цикл игры
//    LaunchedEffect(Unit) {
//        // Запускаем корутину для игрового цикла
//        launch {
//            while (true) {
//                generateCoins()
//                val coinsToRemove = mutableListOf<Coin>() // Список для удаления монет
//                coins.forEach { coin ->
//                    coin.y += 5 // Увеличение y-координаты монеты
//                    if (checkCollision(coin)) {
//                        score++
//                        coinsToRemove.add(coin) // Добавляем монету в список для удаления
//                    }
//                }
//                coins.removeAll(coinsToRemove) // Удаляем монеты после итерации
//                coins.removeAll { it.y > 600f } // Удаляем монеты, которые вышли за экран
//                kotlinx.coroutines.delay(50) // Задержка для управления скоростью игры
//            }
//        }
//    }
//
//    Canvas(modifier = Modifier.fillMaxSize().pointerInput(Unit) {
//        detectDragGestures { _, dragAmount ->
//            basketPosition += dragAmount.x // Перемещение корзинки по экрану
//            basketPosition = basketPosition.coerceIn(0f, 300f) // Ограничение положения корзинки
//        }
//    }) {
//        // Отрисовка монет
//        coins.forEach { coin ->
//            drawCircle(Color.Yellow, radius = 20f, center = androidx.compose.ui.geometry.Offset(coin.x, coin.y))
//        }
//        // Отрисовка корзинки
//        drawRect(Color.Blue, size = androidx.compose.ui.geometry.Size(100f, 20f), topLeft = androidx.compose.ui.geometry.Offset(basketPosition - 50, 550f))
//    }
//
//    // Отображение счета
//    Text(text = "Score: $score", color = Color.Black, modifier = Modifier.padding(16.dp))
//}
//
//data class Coin(var x: Float, var y: Float)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    fillColor: Color = Color.Unspecified,
    outlineColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    outlineDrawStyle: Stroke = Stroke(
        width = 20f,
    ),
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            modifier = Modifier.semantics { invisibleToUser() },
            color = outlineColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = myfont,
            letterSpacing = letterSpacing,
            textDecoration = null,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style.copy(
                shadow = null,
                drawStyle = outlineDrawStyle,
            ),
        )

        Text(
            text = text,
            color = fillColor,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = myfont,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = style,
        )
    }
}



