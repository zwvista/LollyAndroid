package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPhrasesTextbookDetailBinding
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesTextbookDetailActivity : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesUnitViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesUnitDetailViewModel(item) } }
    lateinit var binding: ActivityPhrasesTextbookDetailBinding
    lateinit var item: MUnitPhrase

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MUnitPhrase
        binding = DataBindingUtil.inflate<ActivityPhrasesTextbookDetailBinding>(LayoutInflater.from(this), R.layout.activity_phrases_textbook_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesTextbookDetailActivity
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(this, vm.vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(this, vm.vmSettings.lstParts) { it.label }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.phrase = vm.vmSettings.autoCorrectInput(item.phrase)
        vm.update(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
