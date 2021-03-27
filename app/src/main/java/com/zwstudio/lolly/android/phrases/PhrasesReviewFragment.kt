package com.zwstudio.lolly.android.phrases

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentPhrasesReviewBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.phrases.PhrasesReviewViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class PhrasesReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesReviewViewModel>() }
    var binding by autoCleared<FragmentPhrasesReviewBinding>()
    lateinit var tts: TextToSpeech

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPhrasesReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.title = resources.getString(R.string.phrases_review)
        vm.compositeDisposable = compositeDisposable
        tts = TextToSpeech(requireContext(), this)
        btnNewTest()
    }

    override fun onDestroyView() {
        vm.subscriptionTimer?.dispose()
        super.onDestroyView()
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    fun btnNewTest() {
//        binding.progressBar1.visibility = View.VISIBLE
//        vm.newTest()
//        binding.progressBar1.visibility = View.INVISIBLE
    }

    fun btnCheck() {
//        vm.check()
    }

    fun chkSpeak(isChecked: Boolean) {
//        if (isChecked)
//            tts.speak(vm.currentPhrase, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}