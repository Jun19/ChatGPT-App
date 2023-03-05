package com.jun.template.common.base


import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun runOnUiThread(action: Runnable) {
        activity?.runOnUiThread { action.run() }
    }

    fun toast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show()
    }

    fun toast(res: Int) {
        Toast.makeText(mContext, res, Toast.LENGTH_LONG).show()
    }


}