package com.zwstudio.lolly.android.words

import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.widget.ProgressBar
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.WordsReviewModel
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import org.androidannotations.annotations.ViewById
import java.util.*

@EFragment(R.layout.content_words_review)
class WordsReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: WordsReviewModel
    lateinit var tts: TextToSpeech

    @ViewById
    lateinit var progressBar1: ProgressBar

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.words_review)
        tts = TextToSpeech(context!!, this)

//        compositeDisposable.add(vm.newTest().subscribe {
//
//        })
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

}