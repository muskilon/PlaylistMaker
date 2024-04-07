package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlayListBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NewPlayListFragment : Fragment() {
    private lateinit var binding: FragmentNewPlayListBinding
    private lateinit var navBar: BottomNavigationView
    private val viewModel by activityViewModel<NewPlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navBar = requireActivity().findViewById(R.id.bottomNavigationView)
        navBar.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        navBar.isVisible = true
    }
}