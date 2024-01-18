package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.HistorySharedPreferences
import com.example.playlistmaker.data.HistorySharedPreferences.songsHistory
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.VisibilityState
import com.example.playlistmaker.presentation.ui.MainActivity.Companion.SEARCH_HISTORY_KEY


class SearchActivity : AppCompatActivity(), RenderState {
    private var searchInput: String = EMPTY
    private lateinit var binding: ActivitySearchBinding

    private lateinit var searchResultsAdapter: SearchResultAdapter
    private lateinit var songsHistoryAdapter: SearchResultAdapter
    private var handler: Handler? = null
    private val songs = ArrayList<Track>()
    private val tracksInteractor = Creator.provideTracksInteractor(this)

    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundSongs: Resource<List<Track>>) {
            handler?.post {
                when (foundSongs) {
                    is Resource.ConnectionError -> render(VisibilityState.NO_CONNECTIONS)
                    is Resource.NotFound -> render(VisibilityState.NOT_FOUND)
                    is Resource.Data -> {
                        if (foundSongs.value.isEmpty()) render(VisibilityState.NOT_FOUND)
                        else {
                            songs.clear()
                            songs.addAll(foundSongs.value)
                            render(VisibilityState.SEARCH)
                        }
                    }
                }
            }
            }
        }

    private val searchRunnable = Runnable {
        if (searchInput.isNotEmpty()) {
            render(VisibilityState.PROGRESS_BAR)
            tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        handler = Handler(Looper.getMainLooper())

        searchResultsAdapter = SearchResultAdapter(songs, ItemClickListener(), this)
        songsHistoryAdapter = SearchResultAdapter(songsHistory, ItemClickListener(), this)

        binding.searchResultsRecyclerView.adapter = searchResultsAdapter
        binding.youSearched.songsHistoryRecyclerView.adapter = songsHistoryAdapter

        if (savedInstanceState != null) {
            searchInput = savedInstanceState.getString(INPUT_STRING, "")
            binding.searchBarEdit.setText(searchInput)
        }

        if (sharedPreferences.getString(SEARCH_HISTORY_KEY, null) != null) {
            songsHistory.clear()
            songsHistory.addAll(HistorySharedPreferences.read().songsHistorySaved)
        }

        binding.backArrow.setOnClickListener {
            this.finish()
        }

        binding.noConnectionPlaceholder.refreshButton.setOnClickListener {
            render(VisibilityState.SEARCH)
            tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
        }
        binding.youSearched.clearHistoryButton.setOnClickListener {
            songsHistory.clear()
            HistorySharedPreferences.clear()
            render(VisibilityState.SEARCH)
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
                render(VisibilityState.SEARCH)
                handler?.removeCallbacks(searchRunnable)
                tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
            }
            false
        }

        binding.searchBarEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchBarEdit.text.isNullOrEmpty() && songsHistory.isNotEmpty()) {
                render(VisibilityState.SONG_HISTORY)
            } else {
                render(VisibilityState.SEARCH)
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
                    render(VisibilityState.SONG_HISTORY)
                } else {
                    render(VisibilityState.SEARCH)
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.searchBarEdit.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_STRING, searchInput)
    }

    private fun searchDebounce() {
        handler?.removeCallbacks(searchRunnable)
        handler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

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

    override fun render(state: VisibilityState) {
        when (state) {
            VisibilityState.SEARCH -> showSearch()
            VisibilityState.SONG_HISTORY -> showHistory()
            VisibilityState.NOT_FOUND -> showNotFound()
            VisibilityState.NO_CONNECTIONS -> showNetworkError()
            VisibilityState.PROGRESS_BAR -> showProgressBar()
        }
    }


    companion object {
        private const val INPUT_STRING = "INPUT_STRING"
        private const val EMPTY = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}