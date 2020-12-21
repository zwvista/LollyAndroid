package com.zwstudio.lolly.android.patterns

import android.webkit.WebView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.patterns.PatternsViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_patterns_webpages_browse)
@OptionsMenu(R.menu.menu_save)
class PatternsWebPagesBrowseActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PatternsViewModel

    val compositeDisposable = CompositeDisposable()

    @ViewById
    lateinit var spnWebPages: Spinner
    @ViewById
    lateinit var webView: WebView

    @AfterViews
    fun afterViews() {
        compositeDisposable.add(vm.getWebPages().subscribe {

        })
    }
}
