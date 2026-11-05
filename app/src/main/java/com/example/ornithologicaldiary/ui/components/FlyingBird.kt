package com.example.ornithologicaldiary.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FlyingBird(
    modifier: Modifier = Modifier.size(200.dp),
    color: Color = MaterialTheme.colorScheme.primary
) {
    val transition = rememberInfiniteTransition(label = "flying-bird")

    val flap by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wing-flap"
    )

    val bob by transition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vertical-bob"
    )

    Canvas(modifier = modifier) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f + bob.dp.toPx()
        val span = size.width * 0.42f
        val lift = size.height * (0.08f + 0.26f * flap)

        val path = Path().apply {
            moveTo(centerX - span, centerY)
            quadraticBezierTo(centerX - span * 0.5f, centerY - lift, centerX, centerY)
            quadraticBezierTo(centerX + span * 0.5f, centerY - lift, centerX + span, centerY)
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(
                width = size.minDimension * 0.045f,
                cap = StrokeCap.Round
            )
        )
    }
}
