package com.jun.template.common.extension

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

val Context.connectivityManager: ConnectivityManager get() = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.toast(any: Any? = null) {
    Toast.makeText(this, "$any", Toast.LENGTH_LONG).show()
}

fun Context.toast(res: Int) {
    Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}