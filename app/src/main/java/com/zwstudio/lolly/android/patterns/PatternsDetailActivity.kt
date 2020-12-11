package com.zwstudio.lolly.android.patterns

import android.app.Activity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_detail)
@OptionsMenu(R.menu.menu_save)
class PatternsDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PatternsViewModel
    lateinit var item: MPattern

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etPattern: TextView
    @ViewById
    lateinit var etNote: TextView
    @ViewById
    lateinit var etTags: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("pattern") as MPattern
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        etPattern.text = item.pattern
        etNote.text = item.note
        etTags.text = item.tags
    }

    @OptionsItem
    fun menuSave() {
        item.pattern = vm.vmSettings.autoCorrectInput(etPattern.text.toString())
        item.note = etNote.text.toString()
        item.tags = etTags.text.toString()
        if (item.id == 0)
            compositeDisposable.add(vm.create(item).subscribe {
                item.id = it
            })
        else
            compositeDisposable.add(vm.update(item).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
