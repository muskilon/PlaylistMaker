package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(), RenderState {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private var searchInput: String = EMPTY

    private lateinit var searchResultsAdapter: SearchResultAdapter
    private lateinit var songsHistoryAdapter: SearchResultAdapter

    private val songs = ArrayList<Track>()
    private val songsHistory = ArrayList<Track>()

    private var isClickAllowed = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSongsHistory().observe(viewLifecycleOwner) { liveSongsHistory ->
            with(songsHistory) {
                clear()
                addAll(liveSongsHistory)
            }
        }

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        searchResultsAdapter = SearchResultAdapter(songs) { track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment
                )
            }
        }

        songsHistoryAdapter = SearchResultAdapter(songsHistory) { track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)
                findNavController().navigate(
                    R.id.action_searchFragment_to_playerFragment
                )
            }
        }

        binding.searchResultsRecyclerView.adapter = searchResultsAdapter
        binding.youSearchedIncl.songsHistoryRecyclerView.adapter = songsHistoryAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenState.Loading -> {
                    render(SearchState.PROGRESS_BAR)
                }

                is SearchScreenState.Error -> {
                    render(state.error)
                }

                is SearchScreenState.Content -> {
                    with(songs) {
                        clear()
                        addAll(state.songs)
                    }
                    render(SearchState.SEARCH)
                }
            }
        }

        viewModel.getSongsHistoryFromStorage()

        binding.youSearchedIncl.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.noConnectionPlaceholder.refreshButton.setOnClickListener {
            viewModel.searchSongs(searchInput)
        }

        binding.searchBarInput.setEndIconOnClickListener {
            binding.searchBarEdit.text?.clear()
            imm.hideSoftInputFromWindow(binding.searchBarEdit.windowToken, 0)
            binding.searchBarEdit.clearFocus()
            songs.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        binding.searchBarEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.searchBarEdit.clearFocus()
                viewModel.searchDebounce(EMPTY)
                viewModel.searchSongs(searchInput)
            }
            false
        }

        binding.searchBarEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchBarEdit.text.isNullOrEmpty() && songsHistory.isNotEmpty()) {
                render(SearchState.SONG_HISTORY)
            } else {
                render(SearchState.SEARCH)
            }
        }

        binding.youSearchedIncl.songsHistoryRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                imm.hideSoftInputFromWindow(binding.searchBarEdit.windowToken, 0)
            }
        })

        binding.searchResultsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                imm.hideSoftInputFromWindow(binding.searchBarEdit.windowToken, 0)
            }
        })

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentInput = s.toString()
                if (binding.searchBarEdit.hasFocus() && (currentInput.isEmpty() || currentInput == searchInput) && songsHistory.isNotEmpty()) {
                    render(SearchState.SONG_HISTORY)
                } else {
                    render(SearchState.SEARCH)
                }
                searchInput = currentInput
                viewModel.searchDebounce(currentInput)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.searchBarEdit.addTextChangedListener(simpleTextWatcher)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        songsHistoryAdapter.notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun showSearch() {
        searchResultsAdapter.notifyDataSetChanged()
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.youSearchedIncl.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
        binding.searchResultsRecyclerView.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showHistory() {
        songsHistoryAdapter.notifyDataSetChanged()
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearchedIncl.searchHistory.isVisible = true
    }

    override fun showNotFound() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = true
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearchedIncl.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun showNetworkError() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = true
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearchedIncl.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun showProgressBar() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearchedIncl.searchHistory.isVisible = false
        binding.progressBar.isVisible = true
    }

    override fun render(state: SearchState) {
        when (state) {
            SearchState.SEARCH -> showSearch()
            SearchState.SONG_HISTORY -> showHistory()
            SearchState.NOT_FOUND -> showNotFound()
            SearchState.NO_CONNECTIONS -> showNetworkError()
            SearchState.PROGRESS_BAR -> showProgressBar()
        }
    }


    companion object {
        private const val EMPTY = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}