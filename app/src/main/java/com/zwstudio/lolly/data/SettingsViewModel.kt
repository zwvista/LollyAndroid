package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.domain.Dictionary
import com.zwstudio.lolly.domain.Language
import com.zwstudio.lolly.domain.Textbook
import com.zwstudio.lolly.domain.UserSetting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
class SettingsViewModel {

    val userid = 1

    @App
    lateinit var app: LollyApplication

    var lstUserSettings = listOf<UserSetting>()
    private var selectedUSUserIndex = 0
    val selectedUSUser: UserSetting
        get() = lstUserSettings[selectedUSUserIndex]
    var uslangid: Int
        get() = selectedUSUser.value1?.toInt()!!
        set(value) { selectedUSUser.value1 = value.toString() }
    private var selectedUSLangIndex = 0
    val selectedUSLang: UserSetting
        get() = lstUserSettings[selectedUSLangIndex]
    var ustextbookid: Int
        get() = selectedUSLang.value1?.toInt()!!
        set(value) { selectedUSLang.value1 = value.toString() }
    var usdictid: Int
        get() = selectedUSLang.value2?.toInt()!!
        set(value) { selectedUSLang.value2 = value.toString() }
    private var selectedUSTextbookIndex = 0
    val selectedUSTextbook: UserSetting
        get() = lstUserSettings[selectedUSTextbookIndex]
    var usunitfrom: Int
        get() = selectedUSTextbook.value1?.toInt()!!
        set(value) { selectedUSTextbook.value1 = value.toString() }
    var uspartfrom: Int
        get() = selectedUSTextbook.value2?.toInt()!!
        set(value) { selectedUSTextbook.value2 = value.toString() }
    var usunitto: Int
        get() = selectedUSTextbook.value3?.toInt()!!
        set(value) { selectedUSTextbook.value3 = value.toString() }
    var uspartto: Int
        get() = selectedUSTextbook.value4?.toInt()!!
        set(value) { selectedUSTextbook.value4 = value.toString() }
    val usunitpartfrom: Int
        get() = usunitfrom * 10 + uspartfrom
    val usunitpartto: Int
        get() = usunitto * 10 + uspartto
    val issingleunitpart: Boolean
        get() = usunitpartfrom == usunitpartto

    var lstLanguages = listOf<Language>()
    var selectedLangIndex: Int = 0
    val selectedLang: Language
        get() = lstLanguages[selectedLangIndex]

    var lstTextbooks = listOf<Textbook>()
    var selectedTextbookIndex: Int = 0
        set(value) {
            field = value
            setSelectedTextbookIndex()
        }
    val selectedTextbook: Textbook
        get() = lstTextbooks[selectedTextbookIndex]

    var lstDictionaries = listOf<Dictionary>()
    var selectedDictIndex: Int = 0
        set(value) {
            field = value
            setSelectedDictIndex()
        }
    val selectedDict: Dictionary
        get() = lstDictionaries[selectedDictIndex]

    var word = ""
    var lstUnits = listOf<String>()
    var lstParts = listOf<String>()

    fun getData(onNext: () -> Unit) {
        app.retrofit.create(RestLanguage::class.java)
                .getData()
                .flatMap {
                    lstLanguages = it.lst!!
                    app.retrofit.create(RestUserSetting::class.java)
                            .getDataByUser("USERID,eq,$userid")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    lstUserSettings = it.lst!!
                    selectedUSUserIndex = lstUserSettings.indexOfFirst { it.kind == 1 }
                    setSelectedLangIndex(lstLanguages.indexOfFirst { it.id ==  uslangid }, onNext)
                }
    }

    fun setSelectedLangIndex(langIndex: Int, onNext: () -> Unit) {
        selectedLangIndex = langIndex
        uslangid = selectedLang.id
        selectedUSLangIndex = lstUserSettings.indexOfFirst { it.kind == 2 && it.entityid == uslangid }
        app.retrofit.create(RestDictionary::class.java)
                .getDataByLang("LANGIDFROM,eq,$uslangid")
                .flatMap {
                    lstDictionaries = it.lst!!
                    selectedDictIndex = lstDictionaries.indexOfFirst { it.id == usdictid }
                    app.retrofit.create(RestTextbook::class.java)
                            .getDataByLang("LANGID,eq,$uslangid")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    lstTextbooks = it.lst!!
                    selectedTextbookIndex = lstTextbooks.indexOfFirst { it.id == ustextbookid }
                }
    }

    fun setSelectedTextbookIndex() {
        ustextbookid = selectedTextbook.id
        selectedUSTextbookIndex = lstUserSettings.indexOfFirst { it.kind == 3 && it.entityid == ustextbookid }
        lstUnits = (1..selectedTextbook.units).map { it.toString() }
        lstParts = selectedTextbook.parts?.split(' ')!!
    }

    fun setSelectedDictIndex() {
        usdictid = selectedDict.id
    }
}
