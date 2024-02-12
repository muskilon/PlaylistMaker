package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment(), RenderState {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by activityViewModel<SearchViewModel>()
    private var searchInput: String = EMPTY

    private lateinit var searchResultsAdapter: SearchResultAdapter
    private lateinit var songsHistoryAdapter: SearchResultAdapter

    private val songs = ArrayList<Track>()
    private val songsHistory = ArrayList<Track>()

    private var isClickAllowed = true
    private lateinit var handler: Handler
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        viewModel.getSongsHistory().observe(viewLifecycleOwner) { liveSongsHistory ->
            songsHistory.clear()
            songsHistory.addAll(liveSongsHistory)
        }

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        searchResultsAdapter = SearchResultAdapter(songs) { track ->
            if (clickDebounce()) viewModel.onTrackClick(track)
            val openPlayer = Intent(requireActivity(), PlayerActivity::class.java)
            startActivity(openPlayer)
        }

        songsHistoryAdapter = SearchResultAdapter(songsHistory) { track ->
            if (clickDebounce()) viewModel.onTrackClick(track)
            val openPlayer = Intent(requireActivity(), PlayerActivity::class.java)
            startActivity(openPlayer)
        }

        binding.searchResultsRecyclerView.adapter = searchResultsAdapter
        binding.youSearchedIncl.songsHistoryRecyclerView.adapter = songsHistoryAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchScreenState.Loading -> render(SearchState.PROGRESS_BAR)
                is SearchScreenState.Error -> render(state.error)
                is SearchScreenState.Content -> {
                    songs.clear()
                    songs.addAll(state.songs)
                    render(SearchState.SEARCH)
                }
            }
        }

        viewModel.getSongsHistoryFromStorage()

        binding.youSearchedIncl.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.noConnectionPlaceholder.refreshButton.setOnClickListener {
            viewModel.searchSongs("song", searchInput, "ru")
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
                viewModel.searchSongs("song", searchInput, "ru")
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
                searchInput = s.toString()
                if (binding.searchBarEdit.hasFocus() && s?.isEmpty() == true && songsHistory.isNotEmpty()) {
                    render(SearchState.SONG_HISTORY)
                } else {
                    render(SearchState.SEARCH)
                }
                viewModel.searchDebounce(searchInput)
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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
        binding.searchResultsRecyclerView.isVisible = true
        binding.youSearchedIncl.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
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
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}