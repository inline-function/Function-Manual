package com.fangshen.manual.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy

/**
 * 在屏幕外把一个 Composable 完整渲染成位图并保存到相册。
 *
 * 与局限于窗口高度、依赖页面可见的 PixelCopy 不同：这里把一个不可见的
 * ComposeView 挂进当前 Activity 的 DecorView，以“宽度固定、高度不限”
 * 的方式重新测量，让整页内容（包括长报告）完整排布，再整体 draw 到
 * 一张位图——实现类似系统“长截图”的效果，且不需要跳转到目标页面。
 */
object LongCapture {

    private const val TAG = "LongCapture"

    fun capture(
        context: Context,
        label: String,
        onDone: (Boolean) -> Unit = {},
        content: @Composable () -> Unit,
    ) {
        val activity = context as? Activity
        if (activity == null) {
            Log.w(TAG, "context 不是 Activity，无法长截图")
            onDone(false)
            return
        }
        val widthPx = activity.window.decorView.width.takeIf { it > 0 }
            ?: activity.resources.displayMetrics.widthPixels
        if (widthPx <= 0) { onDone(false); return }

        val composeView = ComposeView(activity).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            // 软件层：确保整棵视图能 draw 到普通（非硬件）Canvas 上
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            setContent(content)
        }
        // 不可见容器：不会在屏幕上出现任何闪烁，但能让组合正常执行
        val container = FrameLayout(activity).apply { visibility = View.INVISIBLE }
        container.addView(
            composeView,
            FrameLayout.LayoutParams(widthPx, ViewGroup.LayoutParams.WRAP_CONTENT),
        )
        (activity.window.decorView as ViewGroup).addView(
            container,
            ViewGroup.LayoutParams(widthPx, ViewGroup.LayoutParams.MATCH_PARENT),
        )

        // 反复测量到高度稳定为止：超长内容（文字换行、FlowRow、表格等）
        // 一次测量拿不到最终高度，直接绘制会出现“尾部大片空白”。
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            widthPx, View.MeasureSpec.EXACTLY,
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            0, View.MeasureSpec.UNSPECIFIED,
        )
        val maxPasses = 8
        var lastHeight = -1
        // schedulePass 的第一次执行会在组合完成后开始
        fun schedulePass(remaining: Int) {
            composeView.post {
                try {
                    composeView.measure(widthSpec, heightSpec)
                    composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)
                    val h = composeView.measuredHeight
                    Log.d(TAG, "测量 pass：h=$h previous=$lastHeight remaining=$remaining")
                    if (h > 0 && h == lastHeight) {
                        drawNow(activity, container, composeView, label, onDone)
                    } else if (remaining > 0) {
                        lastHeight = h
                        schedulePass(remaining - 1)
                    } else {
                        Log.w(TAG, "高度未稳定，按最后一次测量绘制 h=$h")
                        drawNow(activity, container, composeView, label, onDone)
                    }
                } catch (t: Throwable) {
                    Log.e(TAG, "长截图失败", t)
                    finish(activity, container, null, false, onDone)
                }
            }
        }
        schedulePass(maxPasses)
    }

    private fun drawNow(
        activity: Activity,
        container: FrameLayout,
        composeView: ComposeView,
        label: String,
        onDone: (Boolean) -> Unit,
    ) {
        val w = composeView.measuredWidth
        val h = composeView.measuredHeight
        if (w <= 0 || h <= 0) {
            finish(activity, container, null, false, onDone)
            return
        }
        val bitmap = runCatching {
            Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888).apply {
                setDensity(activity.resources.displayMetrics.densityDpi)
            }
        }.getOrNull()
        if (bitmap == null) {
            finish(activity, container, null, false, onDone)
            return
        }
        val drawn = runCatching { composeView.draw(Canvas(bitmap)) }.isSuccess
        if (drawn) {
            val saved = runCatching {
                GalleryCapture.saveToGallery(activity, bitmap, label)
            }.getOrDefault(false)
            finish(activity, container, if (saved) null else bitmap, saved, onDone)
        } else {
            finish(activity, container, bitmap, false, onDone)
        }
    }

    private fun finish(
        activity: Activity,
        container: FrameLayout,
        bitmap: Bitmap?,
        result: Boolean,
        onDone: (Boolean) -> Unit,
    ) {
        runCatching { (activity.window.decorView as ViewGroup).removeView(container) }
        if (result) {
            Toast.makeText(activity, "已保存到相册", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "截图失败，请重试", Toast.LENGTH_SHORT).show()
            bitmap?.recycle()
        }
        onDone(result)
    }
}
