package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsLangDetailBinding
import com.zwstudio.lolly.data.words.WordsLangDetailViewModel
import com.zwstudio.lolly.data.words.WordsLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_words_lang_detail)
@OptionsMenu(R.menu.menu_save)
class WordsLangDetailFragment : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsLangDetailViewModel(item) } }
    lateinit var binding: FragmentWordsLangDetailBinding
    lateinit var item: MLangWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MLangWord
        binding = DataBindingUtil.inflate<FragmentWordsLangDetailBinding>(LayoutInflater.from(this), R.layout.fragment_words_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsLangDetailFragment
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
