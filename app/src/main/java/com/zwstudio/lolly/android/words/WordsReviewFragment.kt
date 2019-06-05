package com.zwstudio.lolly.android.words

import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zwstudio.lolly.data.WordsReviewModel
import com.zwstudio.lolly.data.applyIO
import com.zwstudio.lolly.domain.MSelectItem
import com.zwstudio.lolly.domain.ReviewMode
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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
    var levelge0only = true
    var subscription: Disposable? = null

    @AfterViews
    fun afterViews() {
        activity?.title = resources.getString(com.zwstudio.lolly.android.R.string.words_review)
        tts = TextToSpeech(context!!, this)

        run {
            val lst = vm.vmSettings.lstReviewModes
            val adapter = object : ArrayAdapter<MSelectItem>(context!!, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)!!.label
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
            spnReviewMode.adapter = adapter
        }

        btnNewTest()
    }

    override fun onDestroy() {
        subscription?.dispose()
        super.onDestroy()
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
                tts.speak(vm.currentWord, TextToSpeech.QUEUE_FLUSH, null)
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
        compositeDisposable.add(vm.newTest(shuffled, levelge0only).subscribe {
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
            tts.speak(vm.currentWord, TextToSpeech.QUEUE_FLUSH, null)
    }
    @CheckedChange
    fun chkShuffled(isChecked: Boolean) {
        shuffled = isChecked
    }
    @CheckedChange
    fun chkLevelge0Only(isChecked: Boolean) {
        levelge0only = isChecked
    }

    @ItemSelect
    fun spnReviewModeItemSelected(selected: Boolean, position: Int) {
        vm.mode = ReviewMode.values()[position]
        btnNewTest()
    }
}