/*
 * Copyright (c) 2017. heisenberg.gong
 */

package framework.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.annotation.RequiresApi

object AndroidUtils {

    val DENSITY = Resources.getSystem().displayMetrics.density

    fun getExplicitIntent(context: Context, implicitIntent: Intent): Intent {
        // Retrieve all services that can match the given intent
        val pm = context.packageManager
        val resolveInfo = pm.queryIntentServices(implicitIntent, 0)
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size != 1) {
            Loger.e("get implicitIntent")
            return implicitIntent
        }
        // Get component info and getInstance ComponentName
        val serviceInfo = resolveInfo[0]
        val packageName = serviceInfo.serviceInfo.packageName
        val className = serviceInfo.serviceInfo.name
        val component = ComponentName(packageName, className)
        // Create a new intent. Use the old one for extras and such reuse
        val explicitIntent = Intent(implicitIntent)
        // Set the component to be explicit
        explicitIntent.component = component
        Loger.e("get explicitIntent")
        return explicitIntent
    }

    fun dp2px(dp: Int): Int {
        return Math.round(dp * DENSITY)
    }


    /**
     * 将彩色图转换为纯黑白二色
     *
     * @return 返回转换好的位图
     */
    fun convertToBlackWhite(bmp: Bitmap): Bitmap {
        // 获取位图的宽/高
        val width = bmp.width
        val height = bmp.height
        // 通过位图的大小创建像素点数组
        val pixels = IntArray(width * height)

        bmp.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in 0 until height) {
            for (j in 0 until width) {
                var grey = pixels[width * i + j]

                val alpha = grey and -0x1000000 shr 24
                //分离三原色
                val red = grey and 0x00FF0000 shr 16
                val green = grey and 0x0000FF00 shr 8
                val blue = grey and 0x000000FF

                //转化成灰度像素
                grey = (red * 0.3 + green * 0.59 + blue * 0.11).toInt()
                grey = alpha or (grey shl 16) or (grey shl 8) or grey
                pixels[width * i + j] = grey
            }
        }
        //新建图片
        val newBmp = Bitmap.createBitmap(width, height, Config.RGB_565)
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBmp
        //       Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
        //       return resizeBmp;
    }

    fun blurBitmap(bitmap: Bitmap, context: Context): Bitmap {
        return blurBitmapCore(bitmap, context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun blurBitmapCore(bitmap: Bitmap, context: Context): Bitmap {

        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        val outBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        val rs = RenderScript.create(context)

        // 创建高斯模糊对象
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        val allIn = Allocation.createFromBitmap(rs, bitmap)
        val allOut = Allocation.createFromBitmap(rs, outBitmap)

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(25.0f)

        // Perform the Renderscript
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap)

        // recycle the original bitmap
        bitmap.recycle()

        // After finishing everything, we destroy the Renderscript.
        rs.destroy()

        return outBitmap
    }
}
