package com.naumenko.rickandmorty.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.naumenko.rickandmorty.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: Class<VM>
) : Fragment() {

    protected lateinit var viewModel: VM

    private var _binding: VB? = null
    protected val binding
        get() = checkNotNull(_binding) {
            "Binding isn't init"
        }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDaggerComponent()
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[viewModelClass]
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutRes, container, false)
        _binding = createBinding(view)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun initDaggerComponent()
    abstract fun createBinding(view: View): VB
}