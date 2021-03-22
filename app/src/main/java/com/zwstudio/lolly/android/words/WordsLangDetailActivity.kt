package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityWordsLangDetailBinding
import com.zwstudio.lolly.data.words.WordsLangDetailViewModel
import com.zwstudio.lolly.data.words.WordsLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_lang_detail)
@OptionsMenu(R.menu.menu_save)
class WordsLangDetailActivity : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsLangDetailViewModel(item) } }
    lateinit var binding: ActivityWordsLangDetailBinding
    lateinit var item: MLangWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MLangWord
        binding = DataBindingUtil.inflate<ActivityWordsLangDetailBinding>(LayoutInflater.from(this), R.layout.activity_words_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsLangDetailActivity
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.word = vm.vmSettings.autoCorrectInput(item.word)
        if (item.id == 0)
            vm.create(item)
        else
            vm.update(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
