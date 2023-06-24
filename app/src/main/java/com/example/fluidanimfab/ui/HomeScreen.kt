package com.example.fluidanimfab.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fluidanimfab.ui.theme.FluidAnimFabTheme


@RequiresApi(Build.VERSION_CODES.S)
private fun getRenderEffect(): RenderEffect {
    val blurEffect = RenderEffect
        .createBlurEffect(80f,80f,Shader.TileMode.REPEAT)
    val alphaMatrix = RenderEffect.createColorFilterEffect(
        ColorMatrixColorFilter(
            ColorMatrix(
                floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 0f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 50f, -5000f
                )
            )
        )
    )
    return RenderEffect.createChainEffect(alphaMatrix,blurEffect)

}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val isMenuExtended = remember{ mutableStateOf(false) }

        val animProgress by animateFloatAsState(
            targetValue = if(isMenuExtended.value) 1f else 0f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        )
        val getRenderEffect = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.S){
            getRenderEffect().asComposeRenderEffect()
        }else {
            null
        }

        AnimFabGroup(
            animationProgress = animProgress,
            renderEffect = getRenderEffect
        )
        AnimFabGroup(
            onClick = {
                isMenuExtended.value = !isMenuExtended.value
            },
            animationProgress = animProgress,
            renderEffect = null
        )


    }
}

@Composable
fun AnimFabGroup(
    animationProgress: Float = 0f,
    onClick: () -> Unit = {},
    renderEffect: androidx.compose.ui.graphics.RenderEffect? = null,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .graphicsLayer { this.renderEffect = renderEffect },
        contentAlignment = Alignment.BottomCenter
    ) {

        FloatingButton(
            icon = Icons.Default.Person,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 88.dp
                ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
            ),
        )

        FloatingButton(
            icon = Icons.Default.Lock,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    start = 210.dp
                ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
            ),
        )
        FloatingButton(
            icon = Icons.Default.Call,
            modifier = Modifier.padding(
                PaddingValues(
                    bottom = 72.dp,
                    end = 210.dp
                ) * FastOutSlowInEasing.transform(0f, 0.8f, animationProgress)
            ),
        )

        FloatingButton(
            icon = Icons.Default.Add,
            modifier = Modifier,
            onClick = onClick,
        )
    }
}

@Composable
fun FloatingButton(
    icon: ImageVector?,
    onClick: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    modifier: Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = backgroundColor,
        modifier = modifier,
        shape = CircleShape

    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null,)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FluidAnimFabTheme {
        HomeScreen()
    }
}