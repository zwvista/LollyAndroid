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

    var lstUserSettings = listOf<MUserSetting>()
    private lateinit var selectedUSUser0: MUserSetting
    private lateinit var selectedUSUser1: MUserSetting
    var uslangid: Int
        get() = selectedUSUser0.value1?.toInt()!!
        set(value) {
            selectedUSUser0.value1 = value.toString()
        }
    private lateinit var selectedUSLang2: MUserSetting
    var ustextbookid: Int
        get() = selectedUSLang2.value1?.toInt()!!
        set(value) {
            selectedUSLang2.value1 = value.toString()
        }
    var usdictitem: String
        get() = selectedUSLang2.value2!!
        set(value) {
            selectedUSLang2.value2 = value
        }
    var usdictnoteid: Int
        get() = (selectedUSLang2.value3 ?: "0").toInt()
        set(value) {
            selectedUSLang2.value3 = value.toString()
        }
    var usdictitems: String
        get() = selectedUSLang2.value4 ?: "0"
        set(value) {
            selectedUSLang2.value4 = value
        }
    private lateinit var selectedUSLang3: MUserSetting
    var usvoiceid: Int
        get() = (selectedUSLang3.value4 ?: "0").toInt()
        set(value) {
            selectedUSLang3.value4 = value.toString()
        }
    private lateinit var selectedUSTextbook: MUserSetting
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
    val isSingleUnit: Boolean
        get() = usunitfrom == usunitto && uspartfrom == 1 && uspartto == partCount
    val isInvalidUnitPart: Boolean
        get() = usunitpartfrom > usunitpartto

    var lstLanguages = listOf<MLanguage>()
    lateinit var selectedLang: MLanguage
    val selectedLangIndex: Int
        get() = lstLanguages.indexOf(selectedLang)

    var lstVoices = listOf<MVoice>()
    var selectedVoice: MVoice? = null
        set(value) {
            field = value
            usvoiceid = field?.id ?: 0
        }

    var lstTextbooks = listOf<MTextbook>()
    // https://stackoverflow.com/questions/46366869/kotlin-workaround-for-no-lateinit-when-using-custom-setter
    private var _selectedTextbook: MTextbook? = null
    var selectedTextbook: MTextbook
        get() = _selectedTextbook!!
        set(value) {
            _selectedTextbook = value
            ustextbookid = value.id
            selectedUSTextbook = lstUserSettings.first { it.kind == 11 && it.entityid == value.id }
        }
    val selectedTextbookIndex: Int
        get() = lstTextbooks.indexOf(selectedTextbook)

    var lstDictsMean = listOf<MDictMean>()
    var lstDictItems = listOf<MDictItem>()
    // https://stackoverflow.com/questions/46366869/kotlin-workaround-for-no-lateinit-when-using-custom-setter
    private var _selectedDictItem: MDictItem? = null
    var selectedDictItem: MDictItem
        get() = _selectedDictItem!!
        set(value) {
            _selectedDictItem = value
            usdictitem = selectedDictItem.dictid
        }
    val selectedDictItemIndex: Int
        get() = lstDictItems.indexOf(selectedDictItem)

    var lstDictsNote = listOf<MDictNote>()
    var selectedDictNote: MDictNote? = null
        set(value) {
            field = value
            usdictnoteid = selectedDictNote?.id ?: 0
        }
    val selectedDictNoteIndex: Int
        get() =
            if (selectedDictNote == null) 0
            else lstDictsNote.indexOf(selectedDictNote!!)
    val hasNote: Boolean
        get() = !lstDictsNote.isEmpty()

    val lstUnits: List<MSelectItem>
        get() = selectedTextbook.lstUnits
    val unitCount: Int
        get() = lstUnits.size
    val lstParts: List<MSelectItem>
        get() = selectedTextbook.lstParts
    val partCount: Int
        get() = lstParts.size
    val isSinglePart: Boolean
        get() = partCount == 1

    var lstAutoCorrect = listOf<MAutoCorrect>()

    fun getData() =
        Observables.zip(retrofitJson.create(RestLanguage::class.java).getData(),
            retrofitJson.create(RestUserSetting::class.java).getDataByUser("USERID,eq,$userid"))
        .concatMap {
            lstLanguages = it.first.lst!!
            lstUserSettings = it.second.lst!!
            selectedUSUser0 = lstUserSettings.first { it.kind == 1 && it.entityid == 0 }
            selectedUSUser1 = lstUserSettings.first { it.kind == 1 && it.entityid == 1 }
            setSelectedLang(lstLanguages.first { it.id == uslangid })
        }
        .applyIO()

    fun setSelectedLang(lang: MLanguage): Observable<Unit> {
        selectedLang = lang
        uslangid = selectedLang.id
        selectedUSLang2 = lstUserSettings.first { it.kind == 2 && it.entityid == uslangid }
        selectedUSLang3 = lstUserSettings.first { it.kind == 3 && it.entityid == uslangid }
        val lstDicts = usdictitems.split("\r\n")
        return Observables.zip(retrofitJson.create(RestDictMean::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestDictNote::class.java).getDataByLang("LANGIDFROM,eq,$uslangid"),
            retrofitJson.create(RestTextbook::class.java).getDataByLang("LANGID,eq,$uslangid"),
            retrofitJson.create(RestAutoCorrect::class.java).getDataByLang("LANGID,eq,$uslangid"),
            retrofitJson.create(RestVoice::class.java).getDataByLang("LANGID,eq,$uslangid", "VOICETYPEID,eq,4")) {
            res1, res2, res3, res4, res5 ->
            lstDictsMean = res1.lst!!
            var i = 0
            lstDictItems = lstDicts.flatMap { d ->
                if (d == "0")
                    lstDictsMean.map { MDictItem(it.dictid.toString(), it.dictname!!) }
                else {
                    i++
                    listOf(MDictItem(d, "Custom$i"))
                }
            }
            selectedDictItem = lstDictItems.first { it.dictid == usdictitem }
            lstDictsNote = res2.lst!!
            selectedDictNote =
                if (lstDictsNote.isEmpty()) null
                else lstDictsNote.first { it.id == usdictnoteid }

            fun f(units: String): List<String> {
                var m = Regex("UNITS,(\\d+)").find(units)
                if (m != null) {
                    val units = m.groupValues[1].toInt()
                    return (1..units).map { it.toString() }
                }
                m = Regex("PAGES,(\\d+),(\\d+)").find(units)
                if (m != null) {
                    val n1 = m.groupValues[1].toInt()
                    val n2 = m.groupValues[2].toInt()
                    val units = (n1 + n2 - 1) / n2
                    return (1..units).map { "${it * n2 - n2 + 1}~${it * n2}" }
                }
                m = Regex("CUSTOM,(.+)").find(units)
                if (m != null)
                    return m.groupValues[1].split(",")
                return listOf()
            }
            lstTextbooks = res3.lst!!
            for (o in lstTextbooks) {
                o.lstUnits = f(o.units).mapIndexed { index, s -> MSelectItem(index + 1, s) }
                o.lstParts = o.parts.split(",").mapIndexed { index, s -> MSelectItem(index + 1, s) }
            }
            selectedTextbook = lstTextbooks.first { it.id == ustextbookid }
            lstAutoCorrect = res4.lst!!
            lstVoices = res5.lst!!
            selectedVoice = lstVoices.firstOrNull { it.id == usvoiceid } ?: (if (lstVoices.isEmpty()) null else lstVoices[0])
        }
        .applyIO()
    }

    fun dictHtml(word: String, dictids: List<String>): String {
        var s = "<html><body>\n"
        dictids.forEachIndexed { i, dictid ->
            val item = lstDictsMean.first { it.dictid.toString() == dictid }
            val ifrId = "ifr${i + 1}"
            val url = item.urlString(word, lstAutoCorrect)
            s += "<iframe id='$ifrId' frameborder='1' style='width:100%; height:500px; display:block' src='$url'></iframe>\n"
        }
        s += "</body></html>\n"
        return s
    }

    fun updateLang(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateLang(selectedUSUser0.id, uslangid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateTextbook(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateTextbook(selectedUSLang2.id, ustextbookid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateDictItem(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictItem(selectedUSLang2.id, usdictitem)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateDictNote(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictNote(selectedUSLang2.id, usdictnoteid)
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

    fun updateVoice(): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateVoice(selectedUSLang3.id, usvoiceid)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })
}
