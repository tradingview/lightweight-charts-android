package com.tradingview.lightweightcharts.example.app.view.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.example.app.R

fun Context.chartColor(@ColorRes colorRes: Int, alpha: Int? = null): IntColor {
    val color = ContextCompat.getColor(this, colorRes)
    return (alpha?.let { (color and RGB_MASK) or (it.coerceIn(0, MAX_ALPHA) shl ALPHA_SHIFT) } ?: color)
        .toIntColor()
}

fun Context.chartWatermarkTypography(
    @StyleRes styleRes: Int = R.style.ChartTextWatermark,
): ChartWatermarkTypography {
    val typedArray = obtainStyledAttributes(styleRes, R.styleable.ChartTextWatermark)
    return try {
        ChartWatermarkTypography(
            fontFamily = typedArray.getString(R.styleable.ChartTextWatermark_chartWatermarkFontFamily)
                ?: DEFAULT_FONT_FAMILY,
            titleFontStyle = typedArray.getString(R.styleable.ChartTextWatermark_chartWatermarkTitleFontStyle),
            titleFontSizeLarge = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkTitleFontSizeLarge,
                DEFAULT_TITLE_FONT_SIZE_LARGE,
            ),
            titleFontSizeSmall = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkTitleFontSizeSmall,
                DEFAULT_TITLE_FONT_SIZE_SMALL,
            ),
            titleLineHeightLarge = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkTitleLineHeightLarge,
                DEFAULT_TITLE_LINE_HEIGHT_LARGE,
            ),
            titleLineHeightSmall = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkTitleLineHeightSmall,
                DEFAULT_TITLE_LINE_HEIGHT_SMALL,
            ),
            subtitleFontSizeLarge = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkSubtitleFontSizeLarge,
                DEFAULT_SUBTITLE_FONT_SIZE_LARGE,
            ),
            subtitleFontSizeSmall = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkSubtitleFontSizeSmall,
                DEFAULT_SUBTITLE_FONT_SIZE_SMALL,
            ),
            subtitleLineHeightLarge = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkSubtitleLineHeightLarge,
                DEFAULT_SUBTITLE_LINE_HEIGHT_LARGE,
            ),
            subtitleLineHeightSmall = typedArray.getInt(
                R.styleable.ChartTextWatermark_chartWatermarkSubtitleLineHeightSmall,
                DEFAULT_SUBTITLE_LINE_HEIGHT_SMALL,
            ),
        )
    } finally {
        typedArray.recycle()
    }
}

data class ChartWatermarkTypography(
    val fontFamily: String,
    val titleFontStyle: String?,
    val titleFontSizeLarge: Int,
    val titleFontSizeSmall: Int,
    val titleLineHeightLarge: Int,
    val titleLineHeightSmall: Int,
    val subtitleFontSizeLarge: Int,
    val subtitleFontSizeSmall: Int,
    val subtitleLineHeightLarge: Int,
    val subtitleLineHeightSmall: Int,
) {
    fun titleFontSize(large: Boolean): Int = if (large) titleFontSizeLarge else titleFontSizeSmall

    fun titleLineHeight(large: Boolean): Int = if (large) titleLineHeightLarge else titleLineHeightSmall

    fun subtitleFontSize(large: Boolean): Int = if (large) subtitleFontSizeLarge else subtitleFontSizeSmall

    fun subtitleLineHeight(large: Boolean): Int = if (large) subtitleLineHeightLarge else subtitleLineHeightSmall
}

private const val RGB_MASK = 0x00FFFFFF
private const val ALPHA_SHIFT = 24
private const val MAX_ALPHA = 255
private const val DEFAULT_FONT_FAMILY = "sans-serif"
private const val DEFAULT_TITLE_FONT_SIZE_LARGE = 52
private const val DEFAULT_TITLE_FONT_SIZE_SMALL = 30
private const val DEFAULT_TITLE_LINE_HEIGHT_LARGE = 56
private const val DEFAULT_TITLE_LINE_HEIGHT_SMALL = 34
private const val DEFAULT_SUBTITLE_FONT_SIZE_LARGE = 18
private const val DEFAULT_SUBTITLE_FONT_SIZE_SMALL = 14
private const val DEFAULT_SUBTITLE_LINE_HEIGHT_LARGE = 24
private const val DEFAULT_SUBTITLE_LINE_HEIGHT_SMALL = 18
