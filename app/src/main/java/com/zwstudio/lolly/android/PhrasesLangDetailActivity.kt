package com.zwstudio.lolly.android

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesLangViewModel
import com.zwstudio.lolly.domain.LangPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_lang_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesLangDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesLangViewModel
    lateinit var item: LangPhrase

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstPhrases = (intent.getSerializableExtra("list") as Array<LangPhrase>).toMutableList()
        item = intent.getSerializableExtra("phrase") as LangPhrase
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
    }

    @OptionsItem
    fun menuSave() {
        item.phrase = vm.vmSettings.autoCorrectInput(etPhrase.text.toString())
        item.translation = etTranslation.text.toString()
        if (item.id == 0) {
            vm.lstPhrases.add(item)
            compositeDisposable.add(vm.create(item.langid, item.phrase, item.translation).subscribe {
                item.id = it
            })
        } else
            compositeDisposable.add(vm.update(item.id, item.langid, item.phrase, item.translation).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
