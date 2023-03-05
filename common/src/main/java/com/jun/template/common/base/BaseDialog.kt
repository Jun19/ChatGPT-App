package com.jun.template.common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.jun.template.common.R
import com.jun.template.common.utils.ContextUtils

/**
 *
 *
 * @author Jun
 * @time 2022/6/29
 */
abstract class BaseDialog<VB : ViewBinding>(private val mContext: Context) :
    Dialog(mContext, R.style.RoundDialogTheme) {

    lateinit var vb: VB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = getViewBinding()
        setContentView(vb.root)
        init()
    }

    override fun show() {
        if (ContextUtils.isValidContext(mContext)) {
            super.show()
        }
    }

    abstract fun getViewBinding(): VB
    abstract fun init()
}