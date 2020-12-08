package com.zwstudio.lolly.data.patterns

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.service.wpp.PatternService
import com.zwstudio.lolly.service.wpp.PatternWebPageService
import com.zwstudio.lolly.service.wpp.WebPageService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsViewModel : BaseViewModel() {

    var lstPatternsAll = listOf<MPattern>()
    val lstPatterns = listOf<MPattern>()
    var lstWebPages = mutableListOf<MPatternWebPage>()

    @Bean
    lateinit var patternService: PatternService
    @Bean
    lateinit var patternWebPageService: PatternWebPageService
    @Bean
    lateinit var webPageService: WebPageService

//    val scopeFilter = SimpleStringProperty(SettingsViewModel.lstScopePatternFilters[0])
//    val textFilter = SimpleStringProperty("")
//    val noFilter get() = textFilter.value.isEmpty()
//    val statusText = SimpleStringProperty()

    init {
//        scopeFilter.addListener { _, _, _ -> applyFilters() }
//        textFilter.addListener { _, _, _ -> applyFilters() }
    }

    private fun applyFilters() {
//        lstPatterns.setAll(if (noFilter) lstPatternsAll else lstPatternsAll.filter {
//            (textFilter.value.isEmpty() || (if (scopeFilter.value == "Pattern") it.pattern else if (scopeFilter.value == "Note") it.note else it.tags).contains(textFilter.value, true))
//        })
//        statusText.value = "${lstPatterns.size} Patterns in ${vmSettings.langInfo}"
    }

    fun reload() {
        patternService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .subscribe { lstPatternsAll = it.toMutableList(); applyFilters() }
    }

    fun update(item: MPattern): Observable<Unit> =
        patternService.update(item)
            .applyIO()
    fun create(item: MPattern): Observable<Int> =
        patternService.create(item)
            .applyIO()
    fun delete(id: Int): Observable<Unit> =
        patternService.delete(id)
            .applyIO()
    fun newPattern() = MPattern().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getWebPages(patternid: Int) =
        patternWebPageService.getDataByPattern(patternid)
            .applyIO()
            .doAfterNext { lstWebPages.clear(); lstWebPages.addAll(it) }

    fun updatePatternWebPage(item: MPatternWebPage) =
        patternWebPageService.update(item)
    fun createPatternWebPage(item: MPatternWebPage) =
        patternWebPageService.create(item)
            .applyIO()
            .doAfterNext {
                item.id = it
                lstWebPages.add(item)
            }
    fun deletePatternWebPage(id: Int) =
        patternWebPageService.delete(id)

    fun updateWebPage(item: MPatternWebPage) =
        webPageService.update(item)
    fun createWebPage(item: MPatternWebPage) =
        webPageService.create(item)
            .applyIO()
            .doAfterNext { item.id = it }
    fun deleteWebPage(id: Int) =
        webPageService.delete(id)

    fun newPatternWebPage(patternid: Int, pattern: String) = MPatternWebPage().apply {
        this.patternid = patternid
        this.pattern = pattern
        seqnum = (lstWebPages.maxOfOrNull { it.seqnum } ?: 0) + 1
    }
}
