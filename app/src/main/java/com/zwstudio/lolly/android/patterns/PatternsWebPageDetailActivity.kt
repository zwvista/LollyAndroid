package com.zwstudio.lolly.android.patterns

import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.OptionsMenu

@EActivity(R.layout.activity_patterns_webpage_detail)
@OptionsMenu(R.menu.menu_save)
class PatternsWebPageDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PatternsViewModel
    lateinit var item: MPattern

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
    }
}
