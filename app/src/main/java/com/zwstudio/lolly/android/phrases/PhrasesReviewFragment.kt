package com.zwstudio.lolly.android.phrases

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.databinding.FragmentPhrasesReviewBinding
import com.zwstudio.lolly.android.misc.autoCleared
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.phrases.PhrasesReviewViewModel
import java.util.*

class PhrasesReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesReviewViewModel>() }
    var binding by autoCleared<FragmentPhrasesReviewBinding>()
    lateinit var tts: TextToSpeech

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPhrasesReviewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            model = vm
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun btnNewTest() {
            binding.progressBar1.visibility = View.VISIBLE
            vm.newTest()
            binding.progressBar1.visibility = View.INVISIBLE
        }
        binding.btnNewTest.setOnClickListener { btnNewTest() }
        binding.btnCheck.setOnClickListener { vm.check() }
        binding.chkSpeak.setOnClickListener {
            if (binding.chkSpeak.isChecked)
                tts.speak(vm.currentPhrase, TextToSpeech.QUEUE_FLUSH, null, null)
        }

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
}