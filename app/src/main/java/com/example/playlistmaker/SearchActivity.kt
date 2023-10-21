package com.example.playlistmaker
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SearchActivity : AppCompatActivity() {
    private var searchInput: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val textInput = findViewById<TextInputLayout>(R.id.search_bar_input)
        val textInputEdit = findViewById<TextInputEditText>(R.id.search_bar_edit)

        if (savedInstanceState != null) {
            searchInput = savedInstanceState.getString(INPUT_STRING, "")
            textInputEdit.setText(searchInput)
        }


        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        backArrow.setOnClickListener {
            this.finish()
        }

        textInput.setEndIconOnClickListener {
            textInputEdit.text?.clear()
                textInputEdit.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textInputEdit.windowToken, 0)
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
}