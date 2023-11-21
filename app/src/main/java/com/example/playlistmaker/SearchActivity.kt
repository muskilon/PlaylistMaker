package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchInput: String = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    val itunesService: ItunesAPI = retrofit.create(ItunesAPI::class.java)
    private lateinit var onItemClickListener : OnItemClickListener
    lateinit var searchResultsAdapter : SearchResultAdapter
    lateinit var songsHistoryAdapter : SearchResultAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        onItemClickListener = onItemClickListener()
        searchResultsAdapter = SearchResultAdapter(songs, onItemClickListener)
        songsHistoryAdapter = SearchResultAdapter(songsHistory, onItemClickListener)
        val historyPreferences = HistoryPreferences()

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

        if (sharedPreferences.getString(SEARCH_HISTORY_KEY,null) != null) {
            songsHistory.addAll(historyPreferences.read().songsHistorySaved)
        }

        backArrow.setOnClickListener {
            this.finish()
        }

        refreshButton.setOnClickListener {
            showSearch()
            search(searchInput)
        }
        clearHistoryButton.setOnClickListener {
            songsHistory.clear()
            historyPreferences.clear()
            showSearch()
        }

        searchBarInput.setEndIconOnClickListener {
            searchBarEdit.text?.clear()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchBarEdit.windowToken, 0)
            searchBarEdit.clearFocus()
            songs.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        searchBarEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchBarEdit.clearFocus()
                showSearch()
                search(searchInput)
                searchResultsAdapter.notifyDataSetChanged()
            }
            false
        }
        searchBarEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchBarEdit.text!!.isEmpty() && songsHistory.isNotEmpty()){
                showSongHistory()
            } else {
                showSearch()
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInput = s.toString()
                if (searchBarEdit.hasFocus() && s?.isEmpty() == true && songsHistory.isNotEmpty()){
                    showSongHistory()
                } else {
                    showSearch()
                }
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

    companion object {
        private const val INPUT_STRING = "INPUT_STRING"
    }
}