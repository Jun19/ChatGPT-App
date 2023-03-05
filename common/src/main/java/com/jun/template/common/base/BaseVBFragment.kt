package com.jun.template.common.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    lateinit var vb: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = getBinding(inflater, container)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view, savedInstanceState)
    }

    protected abstract fun getBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): VB

    abstract fun init(view: View, savedInstanceState: Bundle?)

}