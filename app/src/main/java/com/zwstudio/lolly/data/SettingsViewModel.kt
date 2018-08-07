package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.*
import com.zwstudio.lolly.restapi.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
class SettingsViewModel : BaseViewModel1() {

    val userid = 1

    var lstUserSettings = listOf<UserSetting>()
    private var selectedUSUserIndex = 0
    private val selectedUSUser: UserSetting
        get() = lstUserSettings[selectedUSUserIndex]
    var uslangid: Int
        get() = selectedUSUser.value1?.toInt()!!
        set(value) {
            selectedUSUser.value1 = value.toString()
        }
    private var selectedUSLangIndex = 0
    private val selectedUSLang: UserSetting
        get() = lstUserSettings[selectedUSLangIndex]
    var ustextbookid: Int
        get() = selectedUSLang.value1?.toInt()!!
        set(value) {
            selectedUSLang.value1 = value.toString()
        }
    var usdictonlineid: Int
        get() = selectedUSLang.value2?.toInt()!!
        set(value) {
            selectedUSLang.value2 = value.toString()
        }
    var usdictnoteid: Int
        get() = selectedUSLang.value3?.toInt()!!
        set(value) {
            selectedUSLang.value3 = value.toString()
        }
    private var selectedUSTextbookIndex = 0
    private val selectedUSTextbook: UserSetting
        get() = lstUserSettings[selectedUSTextbookIndex]
    var usunitfrom: Int
        get() = selectedUSTextbook.value1?.toInt()!!
        set(value) {
            selectedUSTextbook.value1 = value.toString()
        }
    var uspartfrom: Int
        get() = selectedUSTextbook.value2?.toInt()!!
        set(value) {
            selectedUSTextbook.value2 = value.toString()
        }
    var usunitto: Int
        get() = selectedUSTextbook.value3?.toInt()!!
        set(value) {
            selectedUSTextbook.value3 = value.toString()
        }
    var uspartto: Int
        get() = selectedUSTextbook.value4?.toInt()!!
        set(value) {
            selectedUSTextbook.value4 = value.toString()
        }
    val usunitpartfrom: Int
        get() = usunitfrom * 10 + uspartfrom
    val usunitpartto: Int
        get() = usunitto * 10 + uspartto
    val isSingleUnitPart: Boolean
        get() = usunitpartfrom == usunitpartto
    val isInvalidUnitPart: Boolean
        get() = usunitpartfrom > usunitpartto

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

    var lstDictsOnline = listOf<DictOnline>()
    var selectedDictOnlineIndex: Int = 0
        set(value) {
            field = value
            usdictonlineid = selectedDictOnline.id
        }
    val selectedDictOnline: DictOnline
        get() = lstDictsOnline[selectedDictOnlineIndex]

    var lstDictsNote = listOf<DictNote>()
    var selectedDictNoteIndex: Int = 0
        set(value) {
            field = value
            usdictnoteid = selectedDictNote?.id ?: 0
        }
    val selectedDictNote: DictNote?
        get() =
            if (lstDictsNote.isEmpty())
                null
            else
                lstDictsNote[selectedDictNoteIndex]

    var lstUnits = listOf<String>()
    var lstParts = listOf<String>()

    fun getData() =
        Observables.zip(retrofitJson.create(RestLanguage::class.java).getData(),
            retrofitJson.create(RestUserSetting::class.java).getDataByUser("USERID,eq,$userid"))
        .concatMap {
            lstLanguages = it.first.lst!!
            lstUserSettings = it.second.lst!!
            selectedUSUserIndex = lstUserSettings.indexOfFirst { it.kind == 1 }
            setSelectedLangIndex(lstLanguages.indexOfFirst { it.id == uslangid })
        }
        .applyIO()

    fun setSelectedLangIndex(langIndex: Int): Observable<Unit> {
        selectedLangIndex = langIndex
        uslangid = selectedLang.id
        selectedUSLangIndex = lstUserSettings.indexOfFirst { it.kind == 2 && it.entityid == uslangid }
        return Observables.zip(retrofitJson.create(RestDictOnline::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestDictNote::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestTextbook::class.java).getDataByLang("LANGID,eq,$uslangid"))
        .map {
            lstDictsOnline = it.first.lst!!
            selectedDictOnlineIndex = lstDictsOnline.indexOfFirst { it.id == usdictonlineid }
            lstDictsNote = it.second.lst!!
            if (lstDictsNote.isNotEmpty())
                selectedDictNoteIndex = lstDictsNote.indexOfFirst { it.id == usdictnoteid }
            lstTextbooks = it.third.lst!!
            selectedTextbookIndex = lstTextbooks.indexOfFirst { it.id == ustextbookid }
        }
        .applyIO()
    }

    fun setSelectedTextbookIndex() {
        ustextbookid = selectedTextbook.id
        selectedUSTextbookIndex = lstUserSettings.indexOfFirst { it.kind == 3 && it.entityid == ustextbookid }
        lstUnits = (1..selectedTextbook.units).map { it.toString() }
        lstParts = selectedTextbook.parts?.split(' ')!!
    }

    fun updateLang(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateLang(selectedUSUser.id, uslangid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateTextbook(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateTextbook(selectedUSLang.id, ustextbookid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateDict(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDict(selectedUSLang.id, usdictonlineid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateDictNote(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDict(selectedUSLang.id, usdictnoteid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateUnitFrom(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateUnitFrom(selectedUSTextbook.id, usunitfrom)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updatePartFrom(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updatePartFrom(selectedUSTextbook.id, uspartfrom)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateUnitTo(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateUnitTo(selectedUSTextbook.id, usunitto)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updatePartTo(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updatePartTo(selectedUSTextbook.id, uspartto)
            .map { Log.d("", it.toString()) }
            .applyIO()
}
