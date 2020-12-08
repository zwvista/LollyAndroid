package com.zwstudio.lolly.android.phrases

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_lang_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesLangDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesLangViewModel
    lateinit var item: MLangPhrase

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MLangPhrase
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
    }

    @OptionsItem
    fun menuSave() {
        item.phrase = vm.vmSettings.autoCorrectInput(etPhrase.text.toString())
        item.translation = etTranslation.text.toString()
        if (item.id == 0)
            compositeDisposable.add(vm.create(item.langid, item.phrase, item.translation).subscribe {
                item.id = it
            })
        else
            compositeDisposable.add(vm.update(item.id, item.langid, item.phrase, item.translation).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
