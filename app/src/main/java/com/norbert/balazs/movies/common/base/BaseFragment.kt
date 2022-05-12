package com.norbert.balazs.movies.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {

    private var _layoutBinding: V? = null

    protected val layoutBinding get() = _layoutBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _layoutBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), null, true)
        return layoutBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _layoutBinding = null
    }

    abstract fun getLayoutId(): Int
}