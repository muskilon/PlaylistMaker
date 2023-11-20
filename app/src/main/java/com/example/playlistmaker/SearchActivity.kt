package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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
    val onItemClickListener = object : OnItemClickListener {
        override fun onTrackClick(track: Track) {
            if (songsHistory.isEmpty()) {
                songsHistory.add(track)
            }
            else
                if (songsHistory.contains(track)){
                    songsHistory.remove(track)
                    songsHistory.add(0, track)
                }
                else if (songsHistory.size == 10){
                    songsHistory.removeAt(songsHistory.size - 1)
                    songsHistory.add(0, track)
                }
                else songsHistory.add(0, track)
            Log.d("TAG", "${songsHistory.size}${songsHistory}")
        }
    }
    val searchResultsAdapter = SearchResultAdapter(songs, onItemClickListener)
    val songsHistoryAdapter = SearchResultAdapter(songsHistory, onItemClickListener)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val textInput = findViewById<TextInputLayout>(R.id.search_bar_input)
        val textInputEdit = findViewById<TextInputEditText>(R.id.search_bar_edit)
        val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
        val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
        val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val songsHistoryRecyclerView = findViewById<RecyclerView>(R.id.songsHistoryRecyclerView)
        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val songHistoryView = findViewById<LinearLayout>(R.id.searchHistory)

        searchResultsRecyclerView.adapter = searchResultsAdapter
        songsHistoryRecyclerView.adapter = songsHistoryAdapter

        if (savedInstanceState != null) {
            searchInput = savedInstanceState.getString(INPUT_STRING, "")
            textInputEdit.setText(searchInput)
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
            showSearch()
        }

        textInput.setEndIconOnClickListener {
            textInputEdit.text?.clear()
            textInputEdit.clearFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(textInputEdit.windowToken, 0)
            songs.clear()
            searchResultsAdapter.notifyDataSetChanged()
        }

        textInputEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                showSearch()
                search(searchInput)
                searchResultsAdapter.notifyDataSetChanged()
            }
            false
        }
        textInputEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && textInputEdit.text!!.isEmpty() && songsHistory.isNotEmpty()){
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
                if (textInputEdit.hasFocus() && s?.isEmpty() == true && songsHistory.isNotEmpty()){
                    showSongHistory()
                } else {
                    showSearch()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        textInputEdit.addTextChangedListener(simpleTextWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_STRING, searchInput)
    }

    companion object {
        private const val INPUT_STRING = "INPUT_STRING"
    }
}