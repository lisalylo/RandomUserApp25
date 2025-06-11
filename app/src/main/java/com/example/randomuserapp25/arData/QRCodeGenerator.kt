package com.example.randomuserapp25.arData

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import javax.inject.Inject
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

/**
 * erzeugt aus User-ID QR-code
 */
class QrCodeGenerator @Inject constructor() {
    fun generate(userId: String, size: Int = 512): Bitmap {
        val bitMatrix: BitMatrix =
            MultiFormatWriter().encode(userId, BarcodeFormat.QR_CODE, size, size)
        val bmp = createBitmap(size, size)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp[x, y] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }
        return bmp
    }
}
