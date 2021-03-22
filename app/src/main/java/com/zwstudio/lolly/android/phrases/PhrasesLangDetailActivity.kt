package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPhrasesLangDetailBinding
import com.zwstudio.lolly.data.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_lang_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesLangDetailActivity : AppCompatActivity() {

    val vm by lazy { vita.with(VitaOwner.Multiple(this)).getViewModel<PhrasesLangViewModel>() }
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PhrasesLangDetailViewModel(item) } }
    lateinit var binding: ActivityPhrasesLangDetailBinding
    lateinit var item: MLangPhrase

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MLangPhrase
        binding = DataBindingUtil.inflate<ActivityPhrasesLangDetailBinding>(LayoutInflater.from(this), R.layout.activity_phrases_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesLangDetailActivity
            model = vmDetail
        }
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
