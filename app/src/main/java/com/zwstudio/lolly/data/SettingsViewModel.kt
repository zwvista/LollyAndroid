package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.*
import com.zwstudio.lolly.restapi.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
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
    var usdictpicker: String
        get() = selectedUSLang.value2!!
        set(value) {
            selectedUSLang.value2 = value
        }
    var usdictnoteid: Int
        get() = selectedUSLang.value3?.toInt()!!
        set(value) {
            selectedUSLang.value3 = value.toString()
        }
    var usdictspicker: String
        get() = selectedUSLang.value4 ?: "0"
        set(value) {
            selectedUSLang.value4 = value
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

    var lstDictsWord = listOf<DictWord>()
    var lstDictsPicker = listOf<DictPicker>()
    var selectedDictPickerIndex: Int = 0
        set(value) {
            field = value
            usdictpicker = selectedDictPicker.dictid
        }
    val selectedDictPicker: DictPicker
        get() = lstDictsPicker[selectedDictPickerIndex]

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

    var lstAutoCorrect = listOf<AutoCorrect>()

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
        val lstDicts = usdictspicker.split("\r\n")
        return Observables.zip(retrofitJson.create(RestDictWord::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestDictNote::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestTextbook::class.java).getDataByLang("LANGID,eq,$uslangid"),
            retrofitJson.create(RestAutoCorrect::class.java).getDataByLang("LANGID,eq,$uslangid")) {
            res1, res2, res3, res4 ->
            lstDictsWord = res1.lst!!
            var i = 0
            lstDictsPicker = lstDicts.flatMap { d ->
                if (d == "0")
                    lstDictsWord.map { DictPicker(it.dictid.toString(), it.dictname!!) }
                else {
                    i++;
                    listOf<DictPicker>(DictPicker(d, "Custom$i"))
                }
            }
            selectedDictPickerIndex = lstDictsPicker.indexOfFirst { it.dictid == usdictpicker }
            lstDictsNote = res2.lst!!
            if (lstDictsNote.isNotEmpty())
                selectedDictNoteIndex = lstDictsNote.indexOfFirst { it.id == usdictnoteid }
            lstTextbooks = res3.lst!!
            selectedTextbookIndex = lstTextbooks.indexOfFirst { it.id == ustextbookid }
            lstAutoCorrect = res4.lst!!
        }
        .applyIO()
    }

    fun setSelectedTextbookIndex() {
        ustextbookid = selectedTextbook.id
        selectedUSTextbookIndex = lstUserSettings.indexOfFirst { it.kind == 3 && it.entityid == ustextbookid }
        lstUnits = (1..selectedTextbook.units).map { it.toString() }
        lstParts = selectedTextbook.parts?.split(' ')!!
    }

    fun dictHtml(word: String, dictids: List<String>): String {
        var s = "<html><body>\n"
        dictids.forEachIndexed { i, dictid ->
            val item = lstDictsWord.first { it.dictid.toString() == dictid }
            val ifrId = "ifr${i + 1}"
            val url = item.urlString(word, lstAutoCorrect)
            s += "<iframe id='$ifrId' frameborder='1' style='width:100%; height:500px; display:block' src='$url'></iframe>\n"
        }
        s += "</body></html>\n"
        return s
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

    fun updateDictPicker(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictPicker(selectedUSLang.id, usdictpicker)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateDictNote(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictNote(selectedUSLang.id, usdictnoteid)
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
