package com.zwstudio.lolly.android.phrases

import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.PhrasesReviewModel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EFragment
import java.util.*

@EFragment(R.layout.content_phrases_review)
class PhrasesReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: PhrasesReviewModel
    lateinit var tts: TextToSpeech

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(R.string.phrases_review)
        tts = TextToSpeech(context!!, this);
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