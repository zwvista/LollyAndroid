package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPatternsWebpageDetailBinding
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.data.patterns.PatternsWebPageDetailViewModel
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_webpage_detail)
@OptionsMenu(R.menu.menu_save)
class PatternsWebPageDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PatternsViewModel
    lateinit var vmDetail: PatternsWebPageDetailViewModel
    lateinit var item: MPatternWebPage

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("webpage") as MPatternWebPage
        DataBindingUtil.inflate<ActivityPatternsWebpageDetailBinding>(LayoutInflater.from(this), R.layout.activity_patterns_webpage_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsWebPageDetailActivity
            vmDetail = PatternsWebPageDetailViewModel(item)
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item);
        if (item.id == 0)
            compositeDisposable.add(vm.createPatternWebPage(item).subscribe())
        else
            compositeDisposable.add(vm.updatePatternWebPage(item).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
