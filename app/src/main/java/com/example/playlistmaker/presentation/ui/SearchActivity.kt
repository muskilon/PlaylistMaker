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
import com.example.playlistmaker.HistoryPreferences
import com.example.playlistmaker.HistoryPreferences.songsHistory
import com.example.playlistmaker.ItemClickListener
import com.example.playlistmaker.ItunesAPI
import com.example.playlistmaker.R
import com.example.playlistmaker.Search.search
import com.example.playlistmaker.Search.songs
import com.example.playlistmaker.SearchResultAdapter
import com.example.playlistmaker.VisibilityState
import com.example.playlistmaker.presentation.ui.MainActivity.Companion.SEARCH_HISTORY_KEY
import com.example.playlistmaker.show
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchInput: String = EMPTY
    private val itunesBaseUrl = "https://itunes.apple.com/search/"
    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    val itunesService: ItunesAPI = retrofit.create(ItunesAPI::class.java)
    lateinit var searchResultsAdapter: SearchResultAdapter
    lateinit var songsHistoryAdapter: SearchResultAdapter
    private var handler: Handler? = null
    private val searchRunnable = Runnable {
        if (searchInput.isNotEmpty()) search(searchInput)
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
            songsHistory.addAll(HistoryPreferences.read().songsHistorySaved)
        }

        backArrow.setOnClickListener {
            this.finish()
        }

        refreshButton.setOnClickListener {
            show(VisibilityState.SEARCH)
            search(searchInput)
        }
        clearHistoryButton.setOnClickListener {
            songsHistory.clear()
            HistoryPreferences.clear()
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
                search(searchInput)
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