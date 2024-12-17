package com.dongsu.timely.presentation.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import timber.log.Timber

typealias FragmentInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    protected open val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(this::class.java.simpleName).d("실행")
        Timber.d("onCreate") //BaseFragment로 뜸
        Log.e(this::class.java.simpleName, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(this::class.java.simpleName, "onCreateView")
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(this::class.java.simpleName, "onViewCreated")
        initView()
    }

    abstract fun initView()

    override fun onStart() {
        super.onStart()
        Log.e(this::class.java.simpleName, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(this::class.java.simpleName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(this::class.java.simpleName, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(this::class.java.simpleName, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(this::class.java.simpleName, "onDestroyView")
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(this::class.java.simpleName, "onDestroy")
    }
}