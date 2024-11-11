package com.dongsu.timely.presentation.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseTabFragment<VB : ViewBinding>(
    private val inflate: FragmentInflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    protected open val binding get() = _binding!!

    private var _groupId: Int? = 0
    protected open val groupId get() = _groupId!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractGroupId()
        initView()
    }
    abstract fun initView()

    private fun extractGroupId() {
        _groupId = arguments?.getInt(GROUP_ID) ?: 0
        Log.e("공통",groupId.toString())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}