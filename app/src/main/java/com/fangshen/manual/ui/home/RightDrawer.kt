package com.fangshen.manual.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * 左侧抽屉：面板从屏幕左侧滑入并贴左对齐，点击阴影（scrim）关闭。
 */
@Composable
fun LeftModalDrawer(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .pointerInput(Unit) { detectTapGestures(onTap = { onDismiss() }) },
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }),
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            content()
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

/**
 * 底部弹层（自行实现）：面板从屏幕底部滑入并贴底对齐，点击阴影（scrim）关闭。
 * （material3 的 ModalBottomSheet 是独立 Dialog 窗口，窗口不延伸到导航栏下方，
 * 底部总会漏出一条缝，因此这里改为组合内实现。）
 */
@Composable
fun BottomModalPanel(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.32f))
                    .pointerInput(Unit) { detectTapGestures(onTap = { onDismiss() }) },
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
            exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            content()
        }
    }
}
