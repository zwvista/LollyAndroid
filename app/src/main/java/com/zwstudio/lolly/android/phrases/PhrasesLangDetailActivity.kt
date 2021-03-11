package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPhrasesLangDetailBinding
import com.zwstudio.lolly.data.phrases.PhrasesLangDetailViewModel
import com.zwstudio.lolly.data.phrases.PhrasesLangViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_lang_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesLangDetailActivity : AppCompatActivity() {

    val vm: PhrasesLangViewModel by viewModels()
    lateinit var vmDetail: PhrasesLangDetailViewModel
    lateinit var binding: ActivityPhrasesLangDetailBinding
    lateinit var item: MLangPhrase

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MLangPhrase
        binding = DataBindingUtil.inflate<ActivityPhrasesLangDetailBinding>(LayoutInflater.from(this), R.layout.activity_phrases_lang_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PhrasesLangDetailActivity
            vmDetail = PhrasesLangDetailViewModel(item)
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.phrase = vm.vmSettings.autoCorrectInput(item.phrase)
        if (item.id == 0)
            compositeDisposable.add(vm.create(item).subscribe())
        else
            compositeDisposable.add(vm.update(item).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
