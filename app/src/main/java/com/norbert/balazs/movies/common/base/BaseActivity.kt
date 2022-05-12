package com.norbert.balazs.movies.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    private var _layoutBinding: V? = null

    protected val layoutBinding get() = _layoutBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _layoutBinding = DataBindingUtil.setContentView(this, getLayoutId())
    }

    override fun onDestroy() {
        super.onDestroy()
        _layoutBinding = null
    }

    protected abstract fun getLayoutId(): Int
}