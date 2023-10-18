package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val backArrow = findViewById<ImageView>(R.id.arrow_back)
        backArrow.setOnClickListener {
            val back = Intent(this, MainActivity::class.java)
            startActivity(back)
        }

        val textInput = findViewById<TextInputLayout>(R.id.search_bar_input)
        val textInputEdit = findViewById<TextInputEditText>(R.id.search_bar_edit)
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
                //empty
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        textInputEdit.addTextChangedListener(simpleTextWatcher)
    }
}