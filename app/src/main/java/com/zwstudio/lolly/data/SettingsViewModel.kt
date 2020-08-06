package com.zwstudio.lolly.data

import android.os.Handler
import com.zwstudio.lolly.domain.*
import com.zwstudio.lolly.service.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.Observables
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
class SettingsViewModel : BaseViewModel1() {

    var lstUSMappings = listOf<MUSMapping>()
    var lstUserSettings = listOf<MUserSetting>()
    private fun getUSValue(info: MUserSettingInfo): String? {
        val o = lstUserSettings.find { it.id == info.usersettingid }!!
        return when (info.valueid) {
            1 -> o.value1
            2 -> o.value2
            3 -> o.value3
            4 -> o.value4
            else -> null
        }
    }
    private fun setUSValue(info: MUserSettingInfo, value: String) {
        val o = lstUserSettings.find { it.id == info.usersettingid }!!
        when (info.valueid) {
            1 -> o.value1 = value
            2 -> o.value2 = value
            3 -> o.value3 = value
            4 -> o.value4 = value
            else -> {}
        }
    }
    private var INFO_USLANGID = MUserSettingInfo()
    var uslangid: Int
        get() = getUSValue(INFO_USLANGID)!!.toInt()
        set(value) {
            setUSValue(INFO_USLANGID, value.toString())
        }
    private var INFO_USLEVELCOLORS = MUserSettingInfo()
    var uslevelcolors = mapOf<Int, List<String>>()
    private var INFO_USSCANINTERVAL = MUserSettingInfo()
    val usscaninterval: Int
        get() = getUSValue(INFO_USSCANINTERVAL)!!.toInt()
    private var INFO_USREVIEWINTERVAL = MUserSettingInfo()
    val usreviewinterval: Int
        get() = getUSValue(INFO_USREVIEWINTERVAL)!!.toInt()
    private var INFO_USTEXTBOOKID = MUserSettingInfo()
    var ustextbookid: Int
        get() = getUSValue(INFO_USTEXTBOOKID)!!.toInt()
        set(value) {
            setUSValue(INFO_USTEXTBOOKID, value.toString())
        }
    private var INFO_USDICTREFERENCE = MUserSettingInfo()
    var usdictreference: String
        get() = getUSValue(INFO_USDICTREFERENCE)!!
        set(value) {
            setUSValue(INFO_USDICTREFERENCE, value)
        }
    private var INFO_USDICTNOTE = MUserSettingInfo()
    var usdictnoteid: Int
        get() = (getUSValue(INFO_USDICTNOTE) ?: "0").toInt()
        set(value) {
            setUSValue(INFO_USDICTNOTE, value.toString())
        }
    private var INFO_USDICTSREFERENCE = MUserSettingInfo()
    var usdictsreference: String
        get() = getUSValue(INFO_USDICTSREFERENCE) ?: ""
        set(value) {
            setUSValue(INFO_USDICTSREFERENCE, value)
        }
    private var INFO_USDICTTRANSLATION = MUserSettingInfo()
    var usdicttranslationid: Int
        get() = (getUSValue(INFO_USDICTTRANSLATION) ?: "0").toInt()
        set(value) {
            setUSValue(INFO_USDICTTRANSLATION, value.toString())
        }
    private var INFO_USANDROIDVOICEID = MUserSettingInfo()
    var usvoiceid: Int
        get() = (getUSValue(INFO_USANDROIDVOICEID) ?: "0").toInt()
        set(value) {
            setUSValue(INFO_USANDROIDVOICEID, value.toString())
        }
    private var INFO_USUNITFROM = MUserSettingInfo()
    var usunitfrom: Int
        get() = getUSValue(INFO_USUNITFROM)!!.toInt()
        set(value) {
            setUSValue(INFO_USUNITFROM, value.toString())
        }
    private var INFO_USPARTFROM = MUserSettingInfo()
    var uspartfrom: Int
        get() = getUSValue(INFO_USPARTFROM)!!.toInt()
        set(value) {
            setUSValue(INFO_USPARTFROM, value.toString())
        }
    private var INFO_USUNITTO = MUserSettingInfo()
    var usunitto: Int
        get() = getUSValue(INFO_USUNITTO)!!.toInt()
        set(value) {
            setUSValue(INFO_USUNITTO, value.toString())
        }
    private var INFO_USPARTTO = MUserSettingInfo()
    var uspartto: Int
        get() = getUSValue(INFO_USPARTTO)!!.toInt()
        set(value) {
            setUSValue(INFO_USPARTTO, value.toString())
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
            INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
            INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
            INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
            INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
            toType =
                if (isSingleUnit) 0
                else if (isSingleUnitPart) 1
                else 2
        }
    val selectedTextbookIndex: Int
        get() = lstTextbooks.indexOf(selectedTextbook)

    var lstDictsReference = listOf<MDictionary>()
    // https://stackoverflow.com/questions/46366869/kotlin-workaround-for-no-lateinit-when-using-custom-setter
    private var _selectedDictReference: MDictionary? = null
    var selectedDictReference: MDictionary
        get() = _selectedDictReference!!
        set(value) {
            _selectedDictReference = value
            usdictreference = selectedDictReference.dictid.toString()
        }
    val selectedDictReferenceIndex: Int
        get() = lstDictsReference.indexOf(selectedDictReference)

    var lstDictsNote = listOf<MDictionary>()
    var selectedDictNote: MDictionary? = null
        set(value) {
            field = value
            usdictnoteid = selectedDictNote?.id ?: 0
        }
    val selectedDictNoteIndex: Int
        get() =
            if (selectedDictNote == null) 0
            else lstDictsNote.indexOf(selectedDictNote!!)

    var lstDictsTranslation = listOf<MDictionary>()
    var selectedDictTranslation: MDictionary? = null
        set(value) {
            field = value
            usdicttranslationid = selectedDictTranslation?.id ?: 0
        }
    val selectedDictTranslationIndex: Int
        get() =
            if (selectedDictTranslation == null) 0
            else lstDictsTranslation.indexOf(selectedDictTranslation!!)

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

    val lstReviewModes = ReviewMode.values().mapIndexed { index, s -> MSelectItem(index, s.toString()) }

    @Bean
    lateinit var languageService: LanguageService
    @Bean
    lateinit var usMappingService: USMappingService
    @Bean
    lateinit var userSettingService: UserSettingService
    @Bean
    lateinit var dictionaryService: DictionaryService
    @Bean
    lateinit var textbookService: TextbookService
    @Bean
    lateinit var autoCorrectService: AutoCorrectService
    @Bean
    lateinit var voiceService: VoiceService

    private fun getUSInfo(name: String): MUserSettingInfo {
        val o = lstUSMappings.find { it.name == name }!!
        val entityid = when {
            o.entityid != -1 -> o.entityid
            o.level == 1 -> selectedLang.id
            o.level == 2 -> selectedTextbook.id
            else -> 0
        }
        val o2 = lstUserSettings.find { it.kind == o.kind && it.entityid == entityid }!!
        return MUserSettingInfo(o2.id, o.valueid)
    }

    var handler: Handler? = null
    var settingsListener: SettingsListener? = null
    fun getData(): Observable<Unit> =
        Observables.zip(languageService.getData(),
            usMappingService.getData(),
            userSettingService.getDataByUser(GlobalConstants.userid))
        .concatMap {
            lstLanguages = it.first
            lstUSMappings = it.second
            lstUserSettings = it.third
            INFO_USLANGID = getUSInfo(MUSMapping.NAME_USLANGID)
            INFO_USLEVELCOLORS = getUSInfo(MUSMapping.NAME_USLEVELCOLORS)
            INFO_USSCANINTERVAL = getUSInfo(MUSMapping.NAME_USSCANINTERVAL)
            INFO_USREVIEWINTERVAL = getUSInfo(MUSMapping.NAME_USREVIEWINTERVAL)
            val lst = getUSValue(INFO_USLEVELCOLORS)!!.split("\r\n").map { it.split(',') }
            // https://stackoverflow.com/questions/32935470/how-to-convert-list-to-map-in-kotlin
            uslevelcolors = lst.associateBy({ it[0].toInt() }, { listOf(it[1], it[2]) })
            handler?.post { settingsListener?.onGetData() }
            setSelectedLang(lstLanguages.first { it.id == uslangid })
        }.applyIO()

    fun setSelectedLang(lang: MLanguage): Observable<Unit> {
        val isinit = lang.id == uslangid
        selectedLang = lang
        uslangid = selectedLang.id
        INFO_USTEXTBOOKID = getUSInfo(MUSMapping.NAME_USTEXTBOOKID)
        INFO_USDICTREFERENCE = getUSInfo(MUSMapping.NAME_USDICTREFERENCE)
        INFO_USDICTNOTE = getUSInfo(MUSMapping.NAME_USDICTNOTE)
        INFO_USDICTSREFERENCE = getUSInfo(MUSMapping.NAME_USDICTSREFERENCE)
        INFO_USDICTTRANSLATION = getUSInfo(MUSMapping.NAME_USDICTTRANSLATION)
        INFO_USANDROIDVOICEID = getUSInfo(MUSMapping.NAME_USANDROIDVOICEID)
        return Observables.zip(dictionaryService.getDictsReferenceByLang(uslangid),
            dictionaryService.getDictsNoteByLang(uslangid),
            dictionaryService.getDictsTranslationByLang(uslangid),
            textbookService.getDataByLang(uslangid),
            autoCorrectService.getDataByLang(uslangid),
            voiceService.getDataByLang(uslangid)) {
            res1, res2, res3, res4, res5, res6 ->
            lstDictsReference = res1
            selectedDictReference = lstDictsReference.first { it.dictid.toString() == usdictreference }
            lstDictsNote = res2
            selectedDictNote = lstDictsNote.firstOrNull { it.dictid == usdictnoteid } ?: lstDictsNote.firstOrNull()
            lstDictsTranslation = res3
            selectedDictTranslation = lstDictsTranslation.firstOrNull { it.dictid == usdicttranslationid } ?: lstDictsTranslation.firstOrNull()
            lstTextbooks = res4
            selectedTextbook = lstTextbooks.first { it.id == ustextbookid }
            lstAutoCorrect = res5
            lstVoices = res6
            selectedVoice = lstVoices.firstOrNull { it.id == usvoiceid } ?: lstVoices.firstOrNull()
        }.concatMap {
            if (isinit) {
                handler?.post { settingsListener?.onUpdateLang() }
                Observable.just(Unit)
            } else
                updateLang()
        }.applyIO()
    }

    fun updateLang(): Observable<Unit> =
        userSettingService.update(INFO_USLANGID, uslangid)
            .map { handler?.post { settingsListener?.onUpdateLang() }; Unit }
            .applyIO()

    fun updateTextbook(): Observable<Unit> =
        userSettingService.update(INFO_USTEXTBOOKID, ustextbookid)
            .map { handler?.post { settingsListener?.onUpdateTextbook() }; Unit }
            .applyIO()

    fun updateDictReference(): Observable<Unit> =
        userSettingService.update(INFO_USDICTREFERENCE, usdictreference)
            .map { handler?.post { settingsListener?.onUpdateDictReference() }; Unit }
            .applyIO()

    fun updateDictNote(): Observable<Unit> =
        userSettingService.update(INFO_USDICTNOTE, usdictnoteid)
            .map { handler?.post { settingsListener?.onUpdateDictNote() }; Unit }
            .applyIO()

    fun updateDictTranslation(): Observable<Unit> =
        userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslationid)
            .map { handler?.post { settingsListener?.onUpdateDictTranslation() }; Unit }
            .applyIO()

    fun updateVoice(): Observable<Unit> =
        userSettingService.update(INFO_USANDROIDVOICEID, usvoiceid)
            .map { handler?.post { settingsListener?.onUpdateVoice() }; Unit }
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

    private fun doUpdateUnitFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitfrom == v) return Observable.empty()
        usunitfrom = v
        return userSettingService.update(INFO_USUNITFROM, usunitfrom)
            .map { handler?.post { settingsListener?.onUpdateUnitFrom() }; Unit }
            .applyIO()
    }

    private fun doUpdatePartFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartfrom == v) return Observable.empty()
        uspartfrom = v
        return userSettingService.update(INFO_USPARTFROM, uspartfrom)
            .map { handler?.post { settingsListener?.onUpdatePartFrom() }; Unit }
            .applyIO()
    }

    private fun doUpdateUnitTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitto == v) return Observable.empty()
        usunitto = v
        return userSettingService.update(INFO_USUNITTO, usunitto)
            .map { handler?.post { settingsListener?.onUpdateUnitTo() }; Unit }
            .applyIO()
    }

    private fun doUpdatePartTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartto == v) return Observable.empty()
        uspartto = v
        return userSettingService.update(INFO_USPARTTO, uspartto)
            .map { handler?.post { settingsListener?.onUpdatePartTo() }; Unit }
            .applyIO()
    }
}

interface SettingsListener {
    fun onGetData()
    fun onUpdateLang()
    fun onUpdateTextbook()
    fun onUpdateDictReference()
    fun onUpdateDictNote()
    fun onUpdateDictTranslation()
    fun onUpdateVoice()
    fun onUpdateUnitFrom()
    fun onUpdatePartFrom()
    fun onUpdateUnitTo()
    fun onUpdatePartTo()
}
