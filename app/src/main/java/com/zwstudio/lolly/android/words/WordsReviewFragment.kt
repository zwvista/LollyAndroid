package com.zwstudio.lolly.android.words

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ContentWordsReviewBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.data.words.WordsReviewViewModel
import org.androidannotations.annotations.*
import java.util.*

@EFragment(R.layout.content_words_review)
class WordsReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsReviewViewModel>() }
    var binding by autoCleared<ContentWordsReviewBinding>()
    lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ContentWordsReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(com.zwstudio.lolly.android.R.string.words_review)
        tts = TextToSpeech(requireContext(), this)
        btnNewTest()
    }

    override fun onDestroy() {
        vm.subscriptionTimer?.dispose()
        super.onDestroy()
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    @Click
    fun btnNewTest() {
        binding.progressBar1.visibility = View.VISIBLE
        vm.newTest()
        binding.progressBar1.visibility = View.INVISIBLE
    }

    @Click
    fun btnCheck() {
        vm.check()
    }

    @CheckedChange
    fun chkSpeak(isChecked: Boolean) {
        if (isChecked)
            tts.speak(vm.currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}