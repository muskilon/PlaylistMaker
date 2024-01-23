package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.SearchScreenState
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.Track


class SearchActivity : AppCompatActivity(), RenderState {
    private var searchInput: String = EMPTY
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var searchResultsAdapter: SearchResultAdapter
    private lateinit var songsHistoryAdapter: SearchResultAdapter

    private val songs = ArrayList<Track>()
    private val songsHistory = ArrayList<Track>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(this))[SearchViewModel::class.java]

        viewModel.getSongsHistory().observe(this) { liveSongsHistory ->
            songsHistory.clear()
            songsHistory.addAll(liveSongsHistory)
            Log.d("TAG", songsHistory.size.toString())
        }

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        searchResultsAdapter = SearchResultAdapter(songs, viewModel, this)
        songsHistoryAdapter = SearchResultAdapter(songsHistory, viewModel, this)

        binding.searchResultsRecyclerView.adapter = searchResultsAdapter
        binding.youSearched.songsHistoryRecyclerView.adapter = songsHistoryAdapter

        viewModel.getState().observe(this) { state ->
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

//        if (savedInstanceState != null) {
//            searchInput = savedInstanceState.getString(INPUT_STRING, "")
//            binding.searchBarEdit.setText(searchInput)
//        }

        viewModel.getSongsHistorySharedPreferences()

        binding.youSearched.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.backArrow.setOnClickListener {
            this.finish()
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

        binding.youSearched.songsHistoryRecyclerView.addOnScrollListener(object :
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

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString(INPUT_STRING, searchInput)
//    }


    @SuppressLint("NotifyDataSetChanged")
    override fun showSearch() {
        searchResultsAdapter.notifyDataSetChanged()
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = true
        binding.youSearched.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun showHistory() {
        songsHistoryAdapter.notifyDataSetChanged()
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearched.searchHistory.isVisible = true
    }

    override fun showNotFound() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = true
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearched.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun showNetworkError() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = true
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearched.searchHistory.isVisible = false
        binding.progressBar.isVisible = false
    }

    override fun showProgressBar() {
        binding.notFoundPlaceholder.notFoundPlaceholder.isVisible = false
        binding.noConnectionPlaceholder.noConnectionPlaceholder.isVisible = false
        binding.searchResultsRecyclerView.isVisible = false
        binding.youSearched.searchHistory.isVisible = false
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
        //        private const val INPUT_STRING = "INPUT_STRING"
        private const val EMPTY = ""
    }
}