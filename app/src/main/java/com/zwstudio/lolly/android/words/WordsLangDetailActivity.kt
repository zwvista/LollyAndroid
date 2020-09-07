package com.zwstudio.lolly.android.words

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.WordsLangViewModel
import com.zwstudio.lolly.domain.MLangWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_lang_detail)
@OptionsMenu(R.menu.menu_save)
class WordsLangDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsLangViewModel
    lateinit var item: MLangWord

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etWord: TextView
    @ViewById
    lateinit var etNote: TextView
    @ViewById
    lateinit var tvFamiID: TextView
    @ViewById
    lateinit var tvAccuracy: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MLangWord
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        etWord.text = item.word
        etNote.text = item.note
        tvFamiID.text = "${getResources().getString(R.string.label_famiid)} ${item.famiid}"
        tvAccuracy.text = "${getResources().getString(R.string.label_accuracy)} ${item.accuracy}"
    }

    @OptionsItem
    fun menuSave() {
        item.word = vm.vmSettings.autoCorrectInput(etWord.text.toString())
        item.note = etNote.text.toString()
        val word = vm.vmSettings.autoCorrectInput(item.word)
        if (item.id == 0)
            compositeDisposable.add(vm.create(item.langid, word, item.note).subscribe {
                item.id = it
            })
        else
            compositeDisposable.add(vm.update(item.id, item.langid, word, item.note).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
