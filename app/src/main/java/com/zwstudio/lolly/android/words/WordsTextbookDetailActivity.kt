package com.zwstudio.lolly.android.words

import android.app.Activity
import android.view.LayoutInflater
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityWordsTextbookDetailBinding
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.words.WordsUnitDetailViewModel
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class WordsTextbookDetailActivity : AppCompatActivity() {

    val vm: WordsUnitViewModel by viewModels()
    lateinit var binding: ActivityWordsTextbookDetailBinding
    lateinit var vmDetail: WordsUnitDetailViewModel
    lateinit var item: MUnitWord

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MUnitWord
        binding = DataBindingUtil.inflate<ActivityWordsTextbookDetailBinding>(LayoutInflater.from(this), R.layout.activity_words_textbook_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@WordsTextbookDetailActivity
            vmDetail = WordsUnitDetailViewModel(item)
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
