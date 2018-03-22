package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_words_edit)
class WordsEditActivity : AppCompatActivity() {

    @ViewById
    lateinit var listView: ListView

    @AfterViews
    fun afterViews() {
        title = "Words In Unit(Edit)"
    }
}
