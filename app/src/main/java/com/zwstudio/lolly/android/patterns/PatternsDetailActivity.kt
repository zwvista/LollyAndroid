package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.databinding.ActivityPatternsDetailBinding
import com.zwstudio.lolly.data.patterns.PatternsDetailViewModel
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_detail)
@OptionsMenu(R.menu.menu_save)
class PatternsDetailActivity : AppCompatActivity() {

    val vm: PatternsViewModel by viewModels()
    lateinit var vmDetail: PatternsDetailViewModel
    lateinit var binding: ActivityPatternsDetailBinding
    lateinit var item: MPattern

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("pattern") as MPattern
        binding = DataBindingUtil.inflate<ActivityPatternsDetailBinding>(LayoutInflater.from(this), R.layout.activity_patterns_detail,
                findViewById(android.R.id.content), true).apply {
            lifecycleOwner = this@PatternsDetailActivity
            vmDetail = PatternsDetailViewModel(item)
            model = vmDetail
        }
    }

    @OptionsItem
    fun menuSave() {
        vmDetail.save(item)
        item.pattern = vm.vmSettings.autoCorrectInput(item.pattern)
        if (item.id == 0)
            vm.create(item)
        else
            vm.update(item)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
