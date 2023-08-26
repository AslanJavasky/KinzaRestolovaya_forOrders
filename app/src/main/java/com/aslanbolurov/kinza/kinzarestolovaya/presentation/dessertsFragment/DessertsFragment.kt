package com.aslanbolurov.kinza.kinzarestolovaya.presentation.dessertsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aslanbolurov.kinza.kinzarestolovaya.databinding.FragmentDessertsBinding
import com.aslanbolurov.kinza.kinzarestolovaya.presentation.viewModelDb.ViewModelDb
import com.aslanbolurov.kinza.kinzarestolovaya.presentation.viewModelDb.ViewModelDbFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DessertsFragment : Fragment() {


    @Inject
    lateinit var FVM: DessertsViewModelFactory
    private val viewModel: DessertsViewModel by viewModels { FVM }

    @Inject
    lateinit var FVM_DB: ViewModelDbFactory
    private val viewModelDb: ViewModelDb by viewModels { FVM_DB }

    private var _binding: FragmentDessertsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentDessertsBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter= DessertListAdapter(viewModelDb,viewLifecycleOwner.lifecycleScope )
        binding.rvDesserts.adapter=adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect {
                adapter.submitList(it)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}