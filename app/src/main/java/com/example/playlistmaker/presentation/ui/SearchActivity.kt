package com.example.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.HistorySharedPreferences
import com.example.playlistmaker.data.HistorySharedPreferences.songsHistory
import com.example.playlistmaker.domain.Resource
import com.example.playlistmaker.domain.Track
import com.example.playlistmaker.domain.TracksInteractor
import com.example.playlistmaker.domain.VisibilityState
import com.example.playlistmaker.presentation.ui.MainActivity.Companion.SEARCH_HISTORY_KEY
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SearchActivity : AppCompatActivity() {
    private var searchInput: String = EMPTY

    lateinit var searchResultsAdapter: SearchResultAdapter
    lateinit var songsHistoryAdapter: SearchResultAdapter
    private var handler: Handler? = null
    private val songs = ArrayList<Track>()
    private val tracksInteractor = Creator.provideTracksInteractor()

    private val consumer = object : TracksInteractor.TracksConsumer {
        override fun consume(foundSongs: Resource<List<Track>>) {
            handler?.post {
                when (foundSongs) {
                    is Resource.ConnectionError -> show(VisibilityState.NO_CONNECTIONS)
                    is Resource.NotFound -> show(VisibilityState.NOT_FOUND)
                    is Resource.Data -> {
                        if (foundSongs.value.isEmpty()) show(VisibilityState.NOT_FOUND)
                        else {
                            songs.clear()
                            songs.addAll(foundSongs.value)
                            show(VisibilityState.SEARCH)
                        }
                    }
                }
            }
            }
        }

    private val searchRunnable = Runnable {
        if (searchInput.isNotEmpty()) {
            show(VisibilityState.PROGRESS_BAR)
            tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        handler = Handler(Looper.getMainLooper())

        searchResultsAdapter = SearchResultAdapter(songs, ItemClickListener(), this)
        songsHistoryAdapter = SearchResultAdapter(songsHistory, ItemClickListener(), this)

        val searchBarInput = findViewById<TextInputLayout>(R.id.searchBarInput)
        val searchBarEdit = findViewById<TextInputEditText>(R.id.searchBarEdit)
        val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.searchResultsRecyclerView)
        val songsHistoryRecyclerView = findViewById<RecyclerView>(R.id.songsHistoryRecyclerView)
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        val refreshButton = findViewById<Button>(R.id.refreshButton)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

        searchResultsRecyclerView.adapter = searchResultsAdapter
        songsHistoryRecyclerView.adapter = songsHistoryAdapter

        if (savedInstanceState != null) {
            searchInput = savedInstanceState.getString(INPUT_STRING, "")
            searchBarEdit.setText(searchInput)
        }

        if (sharedPreferences.getString(SEARCH_HISTORY_KEY, null) != null) {
            songsHistory.clear()
            songsHistory.addAll(HistorySharedPreferences.read().songsHistorySaved)
        }

        backArrow.setOnClickListener {
            this.finish()
        }

        refreshButton.setOnClickListener {
            show(VisibilityState.SEARCH)
            tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
        }
        clearHistoryButton.setOnClickListener {
            songsHistory.clear()
            HistorySharedPreferences.clear()
            show(VisibilityState.SEARCH)
        }

        searchBarInput.setEndIconOnClickListener {
            searchBarEdit.text?.clear()
            imm.hideSoftInputFromWindow(searchBarEdit.windowToken, 0)
            searchBarEdit.clearFocus()
            songs.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        searchBarEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchBarEdit.clearFocus()
                show(VisibilityState.SEARCH)
                handler?.removeCallbacks(searchRunnable)
                tracksInteractor.searchSongs("song", searchInput, "ru", consumer)
            }
            false
        }

        searchBarEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchBarEdit.text.isNullOrEmpty() && songsHistory.isNotEmpty()) {
                show(VisibilityState.SONG_HISTORY)
            } else {
                show(VisibilityState.SEARCH)
            }
        }

        songsHistoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                imm.hideSoftInputFromWindow(searchBarEdit.windowToken, 0)
            }
        })

        searchResultsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                imm.hideSoftInputFromWindow(searchBarEdit.windowToken, 0)
            }
        })

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInput = s.toString()
                if (searchBarEdit.hasFocus() && s?.isEmpty() == true && songsHistory.isNotEmpty()) {
                    show(VisibilityState.SONG_HISTORY)
                } else {
                    show(VisibilityState.SEARCH)
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        searchBarEdit.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_STRING, searchInput)
    }

    private fun searchDebounce() {
        handler?.removeCallbacks(searchRunnable)
        handler?.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val INPUT_STRING = "INPUT_STRING"
        private const val EMPTY = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}