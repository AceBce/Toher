package com.example.tolxy


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tolxy.ui.theme.ToLXYTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToLXYTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    GreetingImage(
                        message = stringResource(R.string.what_can_i_say),
                        from = "WZX",
                        modifier = Modifier
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}


@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(8.dp)
    ) {
        Text(text = "To " + stringResource(R.string.toWho),
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.Start)
            )
        Text(
            text = message,
            fontSize = 90.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "From " + stringResource(R.string.from_who),
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)
        )
    }
}

@Composable
fun SequentialFireWorks(showFireWorks: Boolean, number: Int, fireworkTrigger: Int) {

    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    val startXs = remember { List(number) { Random.nextFloat() * 9999f } }

    LaunchedEffect(key1 = currentIndex) {
        delay(Random.nextLong() % 1000)
        if (currentIndex < number)
            currentIndex++
    }

    if (showFireWorks) {
        for (index in 0 until currentIndex){
            FireWork(
                modifier = Modifier.fillMaxSize(),
//                startX = ((index + 1) * Random.nextFloat() * 500f), // 随机生成烟花起始X坐标
                startX = startXs[index],
                color = getRandomColor(),
                fireworkTrigger = fireworkTrigger,
                start = showFireWorks
            )
        }
    }

}

@Composable
fun FireWork(
    modifier: Modifier = Modifier,
    startX: Float,
    color: Color,
    fireworkTrigger: Int,
    start: Boolean
) {
    if (!start)
        return
    var rocketHeight = remember {
        Animatable(0f)
    }

    var explosionRadius = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = fireworkTrigger) {
        rocketHeight.animateTo(
            targetValue = 1800f + Random.nextFloat() * 200f,
            animationSpec = tween(durationMillis = 2000)
        )
        delay(100 + Random.nextLong(100))
        explosionRadius.animateTo(
            targetValue = (300f + (Random.nextFloat() - 0.5) * 200f).toFloat(),
            animationSpec = tween(durationMillis = 1000)
        )
//        delay(100 + Random.nextLong(100))
        explosionRadius.snapTo(-1f)
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val newstartX = startX % canvasWidth


        if (explosionRadius.value == 0f) {
            drawCircle(Color.White, 5f, Offset(newstartX, canvasHeight - rocketHeight.value))
        }

        if (explosionRadius.value > 0f) {
            for (i in 1..30) {
                val angle = (i * (360 / 30)) * (Math.PI / 180)
                val x = cos(angle) * explosionRadius.value + newstartX
                val y = sin(angle) * explosionRadius.value + canvasHeight - rocketHeight.value
                drawCircle(color, 5f, Offset(x.toFloat(), y.toFloat()))
            }
        }
    }

}

fun getRandomColor() : Color {
    val colors = listOf(
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Gray,
        Color.Black,
        Color.White
    )
    // 从列表中随机选择一个颜色并返回
    return colors[Random.nextInt(colors.size)]
}

@Composable
fun GreetingImage(message: String, from: String, modifier: Modifier) {
    val image = painterResource(R.drawable.backgroundimageresized)
    var isVisiable by remember {
        mutableStateOf(true)
    }
    var showFireWorks by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {//Box可以堆叠元素
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop //调整图片适应屏幕
        )
        if (isVisiable) {
            GreetingText(
                message = message,
                from = from,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp))
        }

        var showIndex by remember {
            mutableIntStateOf(0)
        }

        val strings = arrayOf("play", "exit")
        var fireworkTrigger by remember { mutableIntStateOf(0) }

        Button(
            onClick = {
                isVisiable = false
                showFireWorks = true
                showIndex++
                fireworkTrigger++},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-50).dp)
            ) {
            Text(text = strings[showIndex])
        }
        SequentialFireWorks(
            showFireWorks = showFireWorks,
            number = 50,
            fireworkTrigger = fireworkTrigger
        )


    }

}

@Composable
fun Greeting(message: String, from: String, modifier: Modifier) {
    Box(modifier = modifier) {

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToLXYTheme {
        GreetingImage(message = "Happy Birthday", from = "WZX", modifier = Modifier)
    }
}