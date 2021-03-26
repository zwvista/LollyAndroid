package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.FragmentWordsTextbookDetailBinding
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord
import org.androidannotations.annotations.*

@EActivity(R.layout.fragment_words_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class WordsTextbookDetailActivity : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<WordsUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { WordsUnitDetailViewModel(item) } }
    lateinit var binding: FragmentWordsTextbookDetailBinding
    lateinit var item: MUnitWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MUnitWord
        binding = DataBindingUtil.inflate<FragmentWordsTextbookDetailBinding>(LayoutInflater.from(this), R.layout.fragment_words_textbook_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsTextbookDetailActivity
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(this, vm.vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(this, vm.vmSettings.lstParts) { it.label }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.word = vm.vmSettings.autoCorrectInput(item.word)
        vm.update(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
