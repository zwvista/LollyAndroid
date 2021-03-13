package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPatternsWebpagesDetailBinding
import com.zwstudio.lolly.data.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.data.patterns.PatternsWebPagesViewModel
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_webpages_detail)
@OptionsMenu(R.menu.menu_save)
class PatternsWebPagesDetailActivity : AppCompatActivity() {

    val vm: PatternsWebPagesViewModel by viewModels()
    val vmDetail by lazy { vita.with(VitaOwner.Single(this)).getViewModel { PatternsWebPageDetailViewModel(item) } }
    lateinit var binding: ActivityPatternsWebpagesDetailBinding
    lateinit var item: MPatternWebPage

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("webpage") as MPatternWebPage
        binding = DataBindingUtil.inflate<ActivityPatternsWebpagesDetailBinding>(LayoutInflater.from(this), R.layout.activity_patterns_webpages_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsWebPagesDetailActivity
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        if (item.id == 0)
            compositeDisposable.add(vm.createPatternWebPage(item).subscribe())
        else
            compositeDisposable.add(vm.updatePatternWebPage(item).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
