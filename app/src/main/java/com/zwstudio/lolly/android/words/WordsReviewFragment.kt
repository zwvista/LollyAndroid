package com.zwstudio.lolly.android.words

import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsReviewModel
import com.zwstudio.lolly.domain.misc.ReviewMode
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.androidannotations.annotations.*
import java.util.*
import java.util.concurrent.TimeUnit


@EFragment(com.zwstudio.lolly.android.R.layout.content_words_review)
class WordsReviewFragment : Fragment(), TextToSpeech.OnInitListener {

    @Bean
    lateinit var vm: WordsReviewModel
    lateinit var tts: TextToSpeech

    @ViewById
    lateinit var progressBar1: ProgressBar
    @ViewById
    lateinit var tvIndex: TextView
    @ViewById
    lateinit var tvCorrect: TextView
    @ViewById
    lateinit var tvIncorrect: TextView
    @ViewById
    lateinit var tvAccuracy: TextView
    @ViewById
    lateinit var spnReviewMode: Spinner
    @ViewById
    lateinit var btnCheck: Button
    @ViewById
    lateinit var tvWordTarget: TextView
    @ViewById
    lateinit var tvTranslation: TextView
    @ViewById
    lateinit var etWordInput: EditText

    val compositeDisposable = CompositeDisposable()
    var speakOrNot = false
    var shuffled = true
    var subscription: Disposable? = null

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(com.zwstudio.lolly.android.R.string.words_review)
        tts = TextToSpeech(context!!, this)

        run {
            val lst = SettingsViewModel.lstReviewModes
            val adapter = makeAdapter(context!!, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            spnReviewMode.adapter = adapter
        }

        btnNewTest()
    }

    override fun onDestroy() {
        subscription?.dispose()
        super.onDestroy()
        tts?.shutdown()
    }

    override fun onInit(status: Int) {
        if (status != TextToSpeech.SUCCESS) return
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == vm.vmSettings.selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return
        tts.language = locale
    }

    private fun doTest() {
        val b = vm.hasNext
        tvIndex.visibility = if (b) View.VISIBLE else View.INVISIBLE
        tvCorrect.visibility = View.INVISIBLE
        tvIncorrect.visibility = View.INVISIBLE
        tvAccuracy.visibility = if (vm.isTestMode && b) View.VISIBLE else View.INVISIBLE
        btnCheck.isEnabled = b
        tvWordTarget.text = if (vm.isTestMode) "" else vm.currentItem?.wordnote ?: ""
        tvTranslation.visibility = View.VISIBLE
        tvTranslation.text = ""
        etWordInput.setText("", TextView.BufferType.NORMAL)
        etWordInput.visibility = if (vm.mode != ReviewMode.ReviewAuto) View.VISIBLE else View.INVISIBLE
        if (b) {
            tvIndex.text = "${vm.index + 1}/${vm.lstWords.size}"
            tvAccuracy.text = vm.currentItem!!.accuracy
            if (speakOrNot)
                tts.speak(vm.currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
            compositeDisposable.add(vm.getTranslation().subscribe {
                tvTranslation.text = it
            })
        } else {
            subscription?.dispose()
            tvTranslation.visibility = View.INVISIBLE
            etWordInput.visibility = View.INVISIBLE
        }
    }

    @Click
    fun btnNewTest() {
        progressBar1.visibility = View.VISIBLE
        compositeDisposable.add(vm.newTest(shuffled).subscribe {
            doTest()
            progressBar1.visibility = View.INVISIBLE
        })
        btnCheck.text = if (vm.isTestMode) "Check" else "Next"
        if (vm.mode == ReviewMode.ReviewAuto) {
            subscription?.dispose()
            subscription = Observable.interval(vm.vmSettings.usreviewinterval.toLong(), TimeUnit.MILLISECONDS).applyIO().subscribe {
                btnCheck()
            }
            compositeDisposable.add(subscription!!)
        }
    }

    @Click
    fun btnCheck() {
        if (!vm.isTestMode) {
            vm.next()
            doTest()
        } else if (tvCorrect.visibility == View.INVISIBLE && tvIncorrect.visibility == View.INVISIBLE) {
            etWordInput.setText(vm.vmSettings.autoCorrectInput(etWordInput.text.toString()), TextView.BufferType.NORMAL)
            tvWordTarget.visibility = View.VISIBLE
            tvWordTarget.text = vm.currentWord
            if (etWordInput.text.toString() == vm.currentWord)
                tvCorrect.visibility = View.VISIBLE
            else
                tvIncorrect.visibility = View.VISIBLE
            btnCheck.text = "Next"
            compositeDisposable.add(vm.check(etWordInput.text.toString()).subscribe())
        } else {
            vm.next()
            doTest()
            btnCheck.text = "Check"
        }
    }

    @CheckedChange
    fun chkSpeak(isChecked: Boolean) {
        speakOrNot = isChecked
        if (speakOrNot)
            tts.speak(vm.currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
    }
    @CheckedChange
    fun chkShuffled(isChecked: Boolean) {
        shuffled = isChecked
    }

    @ItemSelect
    fun spnReviewModeItemSelected(selected: Boolean, position: Int) {
        vm.mode = ReviewMode.values()[position]
        btnNewTest()
    }
}