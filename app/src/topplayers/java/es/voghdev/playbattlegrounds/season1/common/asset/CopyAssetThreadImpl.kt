package es.voghdev.playbattlegrounds.season1.common.asset

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CopyAssetThreadImpl(val context: Context) : CopyAsset {
    override fun copyAsset(assetFileName: String, destinationPath: String) {
        try {
            val inputStream = context.assets.open(assetFileName)
            val f = File(destinationPath)
            f.createNewFile()
            val outputStream = FileOutputStream(File(destinationPath))
            val buffer = ByteArray(1024)
            var read: Int = inputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                read = inputStream.read(buffer)
            }

            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}