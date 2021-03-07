package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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

    @Bean
    lateinit var vm: PatternsWebPagesViewModel
    lateinit var vmDetail: PatternsWebPageDetailViewModel
    lateinit var item: MPatternWebPage

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("webpage") as MPatternWebPage
        DataBindingUtil.inflate<ActivityPatternsWebpagesDetailBinding>(LayoutInflater.from(this), R.layout.activity_patterns_webpages_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsWebPagesDetailActivity
            vmDetail = PatternsWebPageDetailViewModel(item)
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        if (item.id == 0)
            vm.createPatternWebPage(item)
        else
            vm.updatePatternWebPage(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
