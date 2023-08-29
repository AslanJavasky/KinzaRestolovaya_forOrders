package com.aslanjavasky.kinzarestolovaya.kinzakitchen.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aslanbolurov.kinza.kinzarestolovaya.App
import com.aslanbolurov.kinza.kinzarestolovaya.databinding.FragmentDataFbBinding
import com.aslanbolurov.kinza.kinzarestolovaya.domain.model.Order
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DataFBFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseOrdersAdapter
    private lateinit var manager: LinearLayoutManager

    private var _binding: FragmentDataFbBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataFbBinding.inflate( inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        val options = FirebaseRecyclerOptions.Builder<String>()
            .setQuery(messagesRef, String::class.java)
            .build()
        adapter = FirebaseOrdersAdapter(options)
//        binding.progressBar.visibility = ProgressBar.INVISIBLE
        manager = LinearLayoutManager(App.INSTANCE)
        manager.stackFromEnd = true
        binding.rvOrders.layoutManager = manager
        binding.rvOrders.adapter = adapter

        adapter.registerAdapterDataObserver(
            MyScrollToBottomObserver(binding.rvOrders, adapter, manager)
        )
    }



    override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    companion object {
        const val MESSAGES_CHILD = "Orders"
    }


}