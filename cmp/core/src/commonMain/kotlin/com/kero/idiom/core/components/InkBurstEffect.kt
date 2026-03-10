package com.kero.idiom.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kero.idiom.core.theme.CorrectGreen
import kotlin.random.Random

/**
 * 정답을 맞혔을 때 나타나는 먹물 번짐(Ink Burst) 효과.
 * 초록색(CorrectGreen) 먹물이 빠르고 멀리 퍼져나갑니다.
 */
@Composable
fun InkBurstEffect(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val particles = remember { List(30) { Particle() } } // 파티클 개수 증가 (24 -> 30)
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(600) // 더 빠르게 (1200ms -> 600ms)
        )
        onAnimationEnd()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val centerOffset = androidx.compose.ui.geometry.Offset(centerX, centerY)

        particles.forEach { particle ->
            val pProgress = progress.value
            if (pProgress > 0) {
                val radius = particle.initialRadius * (1f - pProgress * 0.9f)
                val alpha = (1f - pProgress).coerceIn(0f, 1f)
                val distance = particle.speed * pProgress * 800f // 더 멀리 (400f -> 800f)

                val x = centerX + (Math.cos(particle.angle.toDouble()) * distance).toFloat()
                val y = centerY + (Math.sin(particle.angle.toDouble()) * distance).toFloat()

                drawCircle(
                    color = CorrectGreen.copy(alpha = alpha), // 검은색 -> CorrectGreen
                    radius = radius,
                    center = androidx.compose.ui.geometry.Offset(x, y)
                )

                // 불규칙한 먹물 점(Splash) 추가
                if (pProgress < 0.4f) {
                    drawCircle(
                        color = CorrectGreen.copy(alpha = alpha * 0.4f),
                        radius = radius * 2f,
                        center = centerOffset
                    )
                }
            }
        }
    }
}

private class Particle {
    val angle = Random.nextFloat() * 2 * Math.PI.toFloat()
    val speed = Random.nextFloat() * 0.5f + 0.5f
    val initialRadius = Random.nextFloat() * 15f + 5f
}
