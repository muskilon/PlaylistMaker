package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private var searchInput: String = ""
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit =
        Retrofit.Builder().baseUrl(itunesBaseUrl).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val itunesService = retrofit.create(ItunesAPI::class.java)
    val searchResultsAdapter = SearchResultAdapter(songs)


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val textInput = findViewById<TextInputLayout>(R.id.search_bar_input)
        val textInputEdit = findViewById<TextInputEditText>(R.id.search_bar_edit)
        val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
        val noConnectionPlaceholder = findViewById<FrameLayout>(R.id.no_connection_placeholder)
        val searchResultsRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        if (savedInstanceState != null) {
            searchInput = savedInstanceState.getString(INPUT_STRING, "")
            textInputEdit.setText(searchInput)
        }


        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        backArrow.setOnClickListener {
            this.finish()
        }
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        refreshButton.setOnClickListener {
            notFoundPlaceholder.visibility = View.GONE
            noConnectionPlaceholder.visibility = View.GONE
            search(searchInput)
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
                notFoundPlaceholder.visibility = View.GONE
                noConnectionPlaceholder.visibility = View.GONE
                search(searchInput)
                true
            }
            false
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchInput = s.toString()
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

    private fun search(queryInput: String) {
        itunesService.getSearch(queryInput).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>, response: Response<SearchResponse>
            ) {
                val notFoundPlaceholder = findViewById<FrameLayout>(R.id.not_found_placeholder)
                if (response.body()?.resultCount == 0) {
                    notFoundPlaceholder.visibility = View.VISIBLE
                } else {
                    songs.clear()
                    songs.addAll(response.body()?.results!!)
                    searchResultsAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                val noConnectionPlaceholder =
                    findViewById<FrameLayout>(R.id.no_connection_placeholder)
                noConnectionPlaceholder.visibility = View.VISIBLE
            }

        })
    }
}