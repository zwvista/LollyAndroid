package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPhrasesUnitDetailBinding
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.misc.makeCustomAdapter
import com.zwstudio.lolly.data.phrases.PhrasesUnitDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_unit_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesUnitDetailActivity : AppCompatActivity() {

    val vm: PhrasesUnitViewModel by viewModels()
    lateinit var vmDetail: PhrasesUnitDetailViewModel
    lateinit var binding: ActivityPhrasesUnitDetailBinding
    lateinit var item: MUnitPhrase

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MUnitPhrase
        binding = DataBindingUtil.inflate<ActivityPhrasesUnitDetailBinding>(LayoutInflater.from(this), R.layout.activity_phrases_unit_detail,
            findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesUnitDetailActivity
            vmDetail = PhrasesUnitDetailViewModel(item)
            model = vmDetail
        }
        binding.spnUnit.adapter = makeCustomAdapter(this, vm.vmSettings.lstUnits) { it.label }
        binding.spnPart.adapter = makeCustomAdapter(this, vm.vmSettings.lstParts) { it.label }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.phrase = vm.vmSettings.autoCorrectInput(item.phrase)
        if (item.id == 0)
            vm.create(item)
        else
            vm.update(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
