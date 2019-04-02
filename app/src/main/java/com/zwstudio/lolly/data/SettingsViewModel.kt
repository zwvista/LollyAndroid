package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.*
import com.zwstudio.lolly.service.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
class SettingsViewModel : BaseViewModel1() {

    var lstUserSettings = listOf<MUserSetting>()
    private lateinit var selectedUSUser0: MUserSetting
    private lateinit var selectedUSUser1: MUserSetting
    var uslangid: Int
        get() = selectedUSUser0.value1?.toInt()!!
        set(value) {
            selectedUSUser0.value1 = value.toString()
        }
    var uslevelcolors = mapOf<Int, List<String>>()
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
    val selectedVoiceIndex: Int
        get() =
            if (selectedVoice == null) 0
            else lstVoices.indexOf(selectedVoice!!)

    var lstTextbooks = listOf<MTextbook>()
    // https://stackoverflow.com/questions/46366869/kotlin-workaround-for-no-lateinit-when-using-custom-setter
    private var _selectedTextbook: MTextbook? = null
    var selectedTextbook: MTextbook
        get() = _selectedTextbook!!
        set(value) {
            _selectedTextbook = value
            ustextbookid = value.id
            selectedUSTextbook = lstUserSettings.first { it.kind == 11 && it.entityid == value.id }
            toType =
                if (isSingleUnit) 0
                else if (isSingleUnitPart) 1
                else 2
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

    val lstToTypes = listOf("Unit", "Part", "To").mapIndexed { index, s -> MSelectItem(index, s) }
    var toType = 0

    @Bean
    lateinit var languageService: LanguageService;
    @Bean
    lateinit var userSettingService: UserSettingService;
    @Bean
    lateinit var dictMeanService: DictMeanService;
    @Bean
    lateinit var dictNoteService: DictNoteService;
    @Bean
    lateinit var textbookService: TextbookService;
    @Bean
    lateinit var autoCorrectService: AutoCorrectService;
    @Bean
    lateinit var voiceService: VoiceService;

    var onGetData = Observable.just(Unit)
    fun getData(): Observable<Unit> =
        Observables.zip(languageService.getData(),
            userSettingService.getDataByUser(GlobalConstants.userid))
        .concatMap {
            lstLanguages = it.first
            lstUserSettings = it.second
            selectedUSUser0 = lstUserSettings.first { it.kind == 1 && it.entityid == 0 }
            selectedUSUser1 = lstUserSettings.first { it.kind == 1 && it.entityid == 1 }
            val lst = selectedUSUser0.value4!!.split("\r\n").map { it.split(',') }
            // https://stackoverflow.com/questions/32935470/how-to-convert-list-to-map-in-kotlin
            uslevelcolors = lst.associateBy({ it[0].toInt() }, { listOf(it[1], it[2]) })
            onGetData
        }.concatMap {
            setSelectedLang(lstLanguages.first { it.id == uslangid })
        }.applyIO()

    var onUpdateLang = Observable.just(Unit)
    fun setSelectedLang(lang: MLanguage): Observable<Unit> {
        val isinit = lang.id == uslangid
        selectedLang = lang
        uslangid = selectedLang.id
        selectedUSLang2 = lstUserSettings.first { it.kind == 2 && it.entityid == uslangid }
        selectedUSLang3 = lstUserSettings.first { it.kind == 3 && it.entityid == uslangid }
        val lstDicts = usdictitems.split("\r\n")
        return Observables.zip(dictMeanService.getDataByLang(uslangid),
            dictNoteService.getDataByLang(uslangid),
            textbookService.getDataByLang(uslangid),
            autoCorrectService.getDataByLang(uslangid),
            voiceService.getDataByLang(uslangid)) {
            res1, res2, res3, res4, res5 ->
            lstDictsMean = res1
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
            lstDictsNote = res2
            selectedDictNote = lstDictsNote.firstOrNull { it.dictid == usdictnoteid } ?: lstDictsNote.firstOrNull()
            lstTextbooks = res3
            selectedTextbook = lstTextbooks.first { it.id == ustextbookid }
            lstAutoCorrect = res4
            lstVoices = res5
            selectedVoice = lstVoices.firstOrNull { it.id == usvoiceid } ?: lstVoices.firstOrNull()
        }.concatMap {
            if (isinit)
                onUpdateLang
            else
                updateLang()
        }.applyIO()
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

    fun updateLang(): Observable<Unit> =
        userSettingService.updateLang(selectedUSUser0.id, uslangid)
            .concatMap { onUpdateLang }
            .applyIO()

    var onUpdateTextbook = Observable.just(Unit)
    fun updateTextbook(): Observable<Unit> =
        userSettingService.updateTextbook(selectedUSLang2.id, ustextbookid)
            .concatMap { onUpdateTextbook }
            .applyIO()

    var onUpdateDictItem = Observable.just(Unit)
    fun updateDictItem(): Observable<Unit> =
        userSettingService.updateDictItem(selectedUSLang2.id, usdictitem)
            .concatMap { onUpdateDictItem }
            .applyIO()

    var onUpdateDictNote = Observable.just(Unit)
    fun updateDictNote(): Observable<Unit> =
        userSettingService.updateDictNote(selectedUSLang2.id, usdictnoteid)
            .concatMap { onUpdateDictNote }
            .applyIO()

    var onUpdateVoice = Observable.just(Unit)
    fun updateVoice(): Observable<Unit> =
        userSettingService.updateVoice(selectedUSLang3.id, usvoiceid)
            .concatMap { onUpdateVoice }
            .applyIO()

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun updateUnitFrom(v: Int): Observable<Unit> =
        doUpdateUnitFrom(v, false).concatMap {
            if (toType == 0)
                doUpdateSingleUnit()
            else if (toType == 1 || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.empty()
        }

    fun updatePartFrom(v: Int): Observable<Unit> =
        doUpdatePartFrom(v, false).concatMap {
            if (toType == 1 || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.empty()
        }

    fun updateToType(v: Int): Observable<Unit> {
        toType = v
        return if (toType == 0)
            doUpdateSingleUnit()
        else if (toType == 1)
            doUpdateUnitPartTo()
        else
            Observable.empty()
    }

    fun previousUnitPart(): Observable<Unit> =
        if (toType == 0)
            if (usunitfrom > 1)
                Observables.zip(doUpdateUnitFrom(usunitfrom - 1), doUpdateUnitTo(usunitfrom)).map { Unit }
            else
                Observable.empty()
        else if (uspartfrom > 1)
            Observables.zip(doUpdatePartFrom(uspartfrom - 1), doUpdateUnitPartTo()).map { Unit }
        else if (usunitfrom > 1)
            Observables.zip(doUpdateUnitFrom(usunitfrom - 1), doUpdatePartFrom(partCount), doUpdateUnitPartTo()).map { Unit }
        else
            Observable.empty()

    fun nextUnitPart(): Observable<Unit> =
        if (toType == 0)
            if (usunitfrom < unitCount)
                Observables.zip(doUpdateUnitFrom(usunitfrom + 1), doUpdateUnitTo(usunitfrom)).map { Unit }
            else
                Observable.empty()
        else if (uspartfrom < partCount)
            Observables.zip(doUpdatePartFrom(uspartfrom + 1), doUpdateUnitPartTo()).map { Unit }
        else if (usunitfrom < unitCount)
            Observables.zip(doUpdateUnitFrom(usunitfrom + 1), doUpdatePartFrom(1), doUpdateUnitPartTo()).map { Unit }
        else
            Observable.empty()

    fun updateUnitTo(v: Int): Observable<Unit> =
        doUpdateUnitTo(v, false).concatMap {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Observable.empty()
        }

    fun updatePartTo(v: Int): Observable<Unit> =
        doUpdatePartTo(v, false).concatMap {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Observable.empty()
        }

    private fun doUpdateUnitPartFrom(): Observable<Unit> =
        Observables.zip(doUpdateUnitFrom(usunitto), doUpdatePartFrom(uspartto)).map { Unit }

    private fun doUpdateUnitPartTo(): Observable<Unit> =
        Observables.zip(doUpdateUnitTo(usunitfrom), doUpdatePartTo(uspartfrom)).map { Unit }

    private fun doUpdateSingleUnit(): Observable<Unit> =
        Observables.zip(doUpdateUnitTo(usunitfrom), doUpdatePartFrom(1), doUpdatePartTo(partCount)).map { Unit }

    var onUpdateUnitFrom = Observable.just(Unit)
    private fun doUpdateUnitFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitfrom == v) return Observable.empty()
        usunitfrom = v
        return userSettingService.updateUnitFrom(selectedUSTextbook.id, usunitfrom)
            .concatMap { onUpdateUnitFrom }
            .applyIO()
    }

    var onUpdatePartFrom = Observable.just(Unit)
    private fun doUpdatePartFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartfrom == v) return Observable.empty()
        uspartfrom = v
        return userSettingService.updatePartFrom(selectedUSTextbook.id, uspartfrom)
            .concatMap { onUpdatePartFrom }
            .applyIO()
    }

    var onUpdateUnitTo = Observable.just(Unit)
    private fun doUpdateUnitTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitto == v) return Observable.empty()
        usunitto = v
        return userSettingService.updateUnitTo(selectedUSTextbook.id, usunitto)
            .concatMap { onUpdateUnitTo }
            .applyIO()
    }

    var onUpdatePartTo = Observable.just(Unit)
    private fun doUpdatePartTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartto == v) return Observable.empty()
        uspartto = v
        return userSettingService.updatePartTo(selectedUSTextbook.id, uspartto)
            .concatMap { onUpdatePartTo }
            .applyIO()
    }
}
