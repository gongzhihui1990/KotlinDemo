package framework.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author tww
 */
object NetUtil {

    fun isConnected(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conn.activeNetworkInfo
        return info != null && info.isConnected
    }
}
