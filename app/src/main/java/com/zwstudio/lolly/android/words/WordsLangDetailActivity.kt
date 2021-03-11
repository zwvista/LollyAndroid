package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityWordsLangDetailBinding
import com.zwstudio.lolly.data.words.WordsLangDetailViewModel
import com.zwstudio.lolly.data.words.WordsLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_lang_detail)
@OptionsMenu(R.menu.menu_save)
class WordsLangDetailActivity : AppCompatActivity() {

    val vm: WordsLangViewModel by viewModels()
    lateinit var vmDetail: WordsLangDetailViewModel
    lateinit var binding: ActivityWordsLangDetailBinding
    lateinit var item: MLangWord

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MLangWord
        binding = DataBindingUtil.inflate<ActivityWordsLangDetailBinding>(LayoutInflater.from(this), R.layout.activity_words_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsLangDetailActivity
            vmDetail = WordsLangDetailViewModel(item)
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.word = vm.vmSettings.autoCorrectInput(item.word)
        if (item.id == 0)
            compositeDisposable.add(vm.create(item).subscribe())
        else
            compositeDisposable.add(vm.update(item).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
