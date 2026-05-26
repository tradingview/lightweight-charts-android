package com.tradingview.lightweightcharts.example.app.view.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ScreenshotShare {
    private const val FILE_PROVIDER_AUTHORITY = "com.tradingview.fileprovider"

    fun share(context: Context, bitmap: Bitmap, fileName: String = "lightweight-chart-screenshot.png") {
        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.cacheDir
        val file = File(picturesDir, fileName)
        file.parentFile?.mkdirs()

        FileOutputStream(file).use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        }

        val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share image using"))
    }
}
