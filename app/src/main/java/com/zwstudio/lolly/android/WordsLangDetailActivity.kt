package com.zwstudio.lolly.android

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.data.WordsLangViewModel
import com.zwstudio.lolly.domain.LangWord
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_lang_detail)
@OptionsMenu(R.menu.menu_save)
class WordsLangDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsLangViewModel
    lateinit var item: LangWord

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etWord: TextView
    @ViewById
    lateinit var etLevel: TextView
    @ViewById
    lateinit var etNote: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<LangWord>).toMutableList()
        item = intent.getSerializableExtra("word") as LangWord
        tvID.text = "ID: ${item.id}"
        etWord.text = item.word
        etLevel.text = item.level.toString()
        etNote.text = item.note
    }

    @OptionsItem
    fun menuSave() {
        item.word = vm.vmSettings.autoCorrectInput(etWord.text.toString())
        item.level = Integer.parseInt(etLevel.text.toString())
        item.note = etNote.text.toString()
        val word = vm.vmSettings.autoCorrectInput(item.word)
        if (item.id == 0) {
            vm.lstWords.add(item)
            compositeDisposable.add(vm.create(item.langid, word, item.level, item.note).subscribe {
                item.id = it
            })
        } else
            compositeDisposable.add(vm.update(item.id, item.langid, word, item.level, item.note).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
