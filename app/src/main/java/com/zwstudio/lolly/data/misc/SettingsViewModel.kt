package com.zwstudio.lolly.data.misc

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.android.tts
import com.zwstudio.lolly.domain.misc.*
import com.zwstudio.lolly.service.misc.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsViewModel : ViewModel() {

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
    private var INFO_USLANG = MUserSettingInfo()
    var uslang: Int
        get() = getUSValue(INFO_USLANG)!!.toInt()
        set(value) = setUSValue(INFO_USLANG, value.toString())
    private var INFO_USTEXTBOOK = MUserSettingInfo()
    var ustextbook: Int
        get() = getUSValue(INFO_USTEXTBOOK)!!.toInt()
        set(value) = setUSValue(INFO_USTEXTBOOK, value.toString())
    private var INFO_USDICTREFERENCE = MUserSettingInfo()
    var usdictreference: String
        get() = getUSValue(INFO_USDICTREFERENCE)!!
        set(value) = setUSValue(INFO_USDICTREFERENCE, value)
    private var INFO_USDICTNOTE = MUserSettingInfo()
    var usdictnote: Int
        get() = (getUSValue(INFO_USDICTNOTE) ?: "0").toInt()
        set(value) = setUSValue(INFO_USDICTNOTE, value.toString())
    private var INFO_USDICTSREFERENCE = MUserSettingInfo()
    var usdictsreference: String
        get() = getUSValue(INFO_USDICTSREFERENCE) ?: ""
        set(value) = setUSValue(INFO_USDICTSREFERENCE, value)
    private var INFO_USDICTTRANSLATION = MUserSettingInfo()
    var usdicttranslation: Int
        get() = (getUSValue(INFO_USDICTTRANSLATION) ?: "0").toInt()
        set(value) = setUSValue(INFO_USDICTTRANSLATION, value.toString())
    private var INFO_USANDROIDVOICE = MUserSettingInfo()
    var usvoice: Int
        get() = (getUSValue(INFO_USANDROIDVOICE) ?: "0").toInt()
        set(value) = setUSValue(INFO_USANDROIDVOICE, value.toString())
    private var INFO_USUNITFROM = MUserSettingInfo()
    var usunitfrom: Int
        get() = getUSValue(INFO_USUNITFROM)!!.toInt()
        set(value) = setUSValue(INFO_USUNITFROM, value.toString())
    private var INFO_USPARTFROM = MUserSettingInfo()
    var uspartfrom: Int
        get() = getUSValue(INFO_USPARTFROM)!!.toInt()
        set(value) = setUSValue(INFO_USPARTFROM, value.toString())
    private var INFO_USUNITTO = MUserSettingInfo()
    var usunitto: Int
        get() = getUSValue(INFO_USUNITTO)!!.toInt()
        set(value) = setUSValue(INFO_USUNITTO, value.toString())
    private var INFO_USPARTTO = MUserSettingInfo()
    var uspartto: Int
        get() = getUSValue(INFO_USPARTTO)!!.toInt()
        set(value) = setUSValue(INFO_USPARTTO, value.toString())
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
    val selectedLangIndex_= MutableLiveData(-1)
    var selectedLangIndex get() = selectedLangIndex_.value!!; set(v) { selectedLangIndex_.value = v }
    val selectedLang get() = lstLanguages[selectedLangIndex]

    var lstVoices = listOf<MVoice>()
    val selectedVoiceIndex_= MutableLiveData(-1)
    var selectedVoiceIndex get() = selectedVoiceIndex_.value!!; set(v) { selectedVoiceIndex_.value = v }
    val selectedVoice get() = lstVoices.getOrNull(selectedVoiceIndex)

    var lstTextbooks = listOf<MTextbook>()
    val selectedTextbookIndex_= MutableLiveData(-1)
    var selectedTextbookIndex get() = selectedTextbookIndex_.value!!; set(v) { selectedTextbookIndex_.value = v }
    val selectedTextbook get() = lstTextbooks[selectedTextbookIndex]
    var lstTextbookFilters = listOf<MSelectItem>()

    var lstDictsReference = listOf<MDictionary>()
    val selectedDictReferenceIndex_= MutableLiveData(-1)
    var selectedDictReferenceIndex get() = selectedDictReferenceIndex_.value!!; set(v) { selectedDictReferenceIndex_.value = v }
    val selectedDictReference get() = lstDictsReference[selectedDictReferenceIndex]

    var lstDictsNote = listOf<MDictionary>()
    val selectedDictNoteIndex_= MutableLiveData(-1)
    var selectedDictNoteIndex get() = selectedDictNoteIndex_.value!!; set(v) { selectedDictNoteIndex_.value = v }
    val selectedDictNote get() = lstDictsNote.getOrNull(selectedDictNoteIndex)

    var lstDictsTranslation = listOf<MDictionary>()
    val selectedDictTranslationIndex_= MutableLiveData(-1)
    var selectedDictTranslationIndex get() = selectedDictTranslationIndex_.value!!; set(v) { selectedDictTranslationIndex_.value = v }
    val selectedDictTranslation get() = lstDictsTranslation.getOrNull(selectedDictTranslationIndex)

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

    val lstToTypes = UnitPartToType.values().map { v -> MSelectItem(v.ordinal, v.toString()) }
    var toType = UnitPartToType.To

    companion object {
        val lstScopeWordFilters = listOf("Word", "Note").mapIndexed { index, s -> MSelectItem(index, s.toString()) }
        val lstScopePhraseFilters = listOf("Phrase", "Translation").mapIndexed { index, s -> MSelectItem(index, s.toString()) }
        val lstScopePatternFilters = listOf("Pattern", "Note", "Tags").mapIndexed { index, s -> MSelectItem(index, s.toString()) }
        val lstReviewModes = ReviewMode.values().mapIndexed { index, s -> MSelectItem(index, s.toString()) }
        val zeroNote = "O"
    }

    val languageService = LanguageService()
    val usMappingService = USMappingService()
    val userSettingService = UserSettingService()
    val dictionaryService = DictionaryService()
    val textbookService = TextbookService()
    val autoCorrectService = AutoCorrectService()
    val voiceService = VoiceService()
    val htmlService = HtmlService()
    private val compositeDisposable = CompositeDisposable()

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

    var settingsListener: SettingsListener? = null
    fun getData(): Observable<Unit> {
        selectedLangIndex = -1
        selectedDictReferenceIndex = -1
        selectedDictNoteIndex = -1
        selectedDictTranslationIndex = -1
        selectedTextbookIndex = -1
        selectedVoiceIndex = -1
        return Observable.zip(languageService.getData(),
            usMappingService.getData(),
            userSettingService.getDataByUser(GlobalConstants.userid)) { res1, res2, res3 ->
            lstLanguages = res1
            lstUSMappings = res2
            lstUserSettings = res3
            INFO_USLANG = getUSInfo(MUSMapping.NAME_USLANG)
        }.applyIO().map {
            selectedLangIndex = 0.coerceAtLeast(lstLanguages.indexOfFirst { it.id == uslang })
            settingsListener?.onGetData()
        }
    }

    fun updateLang(): Observable<Unit> {
        if (lstLanguages.isEmpty()) return Observable.empty()
        val isinit = selectedLang.id == uslang
        uslang = selectedLang.id
        INFO_USTEXTBOOK = getUSInfo(MUSMapping.NAME_USTEXTBOOK)
        INFO_USDICTREFERENCE = getUSInfo(MUSMapping.NAME_USDICTREFERENCE)
        INFO_USDICTNOTE = getUSInfo(MUSMapping.NAME_USDICTNOTE)
        INFO_USDICTSREFERENCE = getUSInfo(MUSMapping.NAME_USDICTSREFERENCE)
        INFO_USDICTTRANSLATION = getUSInfo(MUSMapping.NAME_USDICTTRANSLATION)
        INFO_USANDROIDVOICE = getUSInfo(MUSMapping.NAME_USANDROIDVOICE)
        return Observable.zip(dictionaryService.getDictsReferenceByLang(uslang),
            dictionaryService.getDictsNoteByLang(uslang),
            dictionaryService.getDictsTranslationByLang(uslang),
            textbookService.getDataByLang(uslang),
            autoCorrectService.getDataByLang(uslang),
            voiceService.getDataByLang(uslang),
            if (isinit) Observable.just(Unit) else userSettingService.update(INFO_USLANG, uslang)) { res1, res2, res3, res4, res5, res6, _ ->
            lstDictsReference = res1
            lstDictsNote = res2
            lstDictsTranslation = res3
            lstTextbooks = res4
            lstTextbookFilters = listOf(MSelectItem(0, "All Textbooks")) + lstTextbooks.map { MSelectItem(it.id, it.textbookname) }
            lstAutoCorrect = res5
            lstVoices = res6
        }.applyIO().map {
            selectedDictReferenceIndex = 0.coerceAtLeast(lstDictsReference.indexOfFirst { it.dictid.toString() == usdictreference })
            selectedDictNoteIndex = 0.coerceAtLeast(lstDictsNote.indexOfFirst { it.dictid == usdictnote })
            selectedDictTranslationIndex = 0.coerceAtLeast(lstDictsTranslation.indexOfFirst { it.dictid == usdicttranslation })
            selectedTextbookIndex = 0.coerceAtLeast(lstTextbooks.indexOfFirst { it.id == ustextbook })
            selectedVoiceIndex = 0.coerceAtLeast(lstVoices.indexOfFirst { it.id == usvoice })
            settingsListener?.onUpdateLang()
        }
    }

    fun updateTextbook(): Observable<Unit> {
        if (lstTextbooks.isEmpty()) return Observable.empty()
        ustextbook = selectedTextbook.id
        INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
        INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
        INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
        INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
        toType = if (isSingleUnit) UnitPartToType.Unit else if (isSingleUnitPart) UnitPartToType.Part else UnitPartToType.To
        return userSettingService.update(INFO_USTEXTBOOK, ustextbook)
            .applyIO()
            .map { settingsListener?.onUpdateTextbook() }
    }

    fun updateDictReference(): Observable<Unit> {
        if (lstDictsReference.isEmpty()) return Observable.empty()
        usdictreference = selectedDictReference.dictid.toString()
        return userSettingService.update(INFO_USDICTREFERENCE, usdictreference)
            .applyIO()
            .map { settingsListener?.onUpdateDictReference() }
    }

    fun updateDictNote(): Observable<Unit> {
        if (lstDictsNote.isEmpty()) return Observable.empty()
        usdictnote = selectedDictNote?.dictid ?: 0
        return userSettingService.update(INFO_USDICTNOTE, usdictnote)
            .applyIO()
            .map { settingsListener?.onUpdateDictNote() }
    }

    fun updateDictTranslation(): Observable<Unit> {
        if (lstDictsTranslation.isEmpty()) return Observable.empty()
        usdicttranslation = selectedDictTranslation?.dictid ?: 0
        return userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslation)
            .applyIO()
            .map { settingsListener?.onUpdateDictTranslation() }
    }

    fun updateVoice(): Observable<Unit> {
        if (lstVoices.isEmpty()) return Observable.empty()
        usvoice = selectedVoice?.id ?: 0
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return Observable.empty()
        tts.language = locale
        return userSettingService.update(INFO_USANDROIDVOICE, usvoice)
            .applyIO()
            .map { settingsListener?.onUpdateVoice() }
    }

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun updateUnitFrom(v: Int): Observable<Unit> =
        doUpdateUnitFrom(v, false).flatMap {
            if (toType == UnitPartToType.Unit)
                doUpdateSingleUnit()
            else if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.just(Unit)
        }

    fun updatePartFrom(v: Int): Observable<Unit> =
        doUpdatePartFrom(v, false).flatMap {
            if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.just(Unit)
        }

    fun updateToType(v: Int): Observable<Unit> {
        toType = UnitPartToType.values()[v]
        return if (toType == UnitPartToType.Unit)
            doUpdateSingleUnit()
        else if (toType == UnitPartToType.Part)
            doUpdateUnitPartTo()
        else
            Observable.just(Unit)
    }

    fun toggleUnitPart(part: Int): Observable<Unit> =
        if (toType == UnitPartToType.Unit) {
            toType = UnitPartToType.Part
            Observables.zip(doUpdatePartFrom(part), doUpdateUnitPartTo()).map { Unit }
        } else if (toType == UnitPartToType.Part) {
            toType = UnitPartToType.Unit
            doUpdateSingleUnit()
        } else
            Observable.just(Unit)

    fun previousUnitPart(): Observable<Unit> =
        if (toType == UnitPartToType.Unit)
            if (usunitfrom > 1)
                Observables.zip(doUpdateUnitFrom(usunitfrom - 1), doUpdateUnitTo(usunitfrom)).map { Unit }
            else
                Observable.just(Unit)
        else if (uspartfrom > 1)
            Observables.zip(doUpdatePartFrom(uspartfrom - 1), doUpdateUnitPartTo()).map { Unit }
        else if (usunitfrom > 1)
            Observables.zip(doUpdateUnitFrom(usunitfrom - 1), doUpdatePartFrom(partCount), doUpdateUnitPartTo()).map { Unit }
        else
            Observable.just(Unit)

    fun nextUnitPart(): Observable<Unit> =
        if (toType == UnitPartToType.Unit)
            if (usunitfrom < unitCount)
                Observables.zip(doUpdateUnitFrom(usunitfrom + 1), doUpdateUnitTo(usunitfrom)).map { Unit }
            else
                Observable.just(Unit)
        else if (uspartfrom < partCount)
            Observables.zip(doUpdatePartFrom(uspartfrom + 1), doUpdateUnitPartTo()).map { Unit }
        else if (usunitfrom < unitCount)
            Observables.zip(doUpdateUnitFrom(usunitfrom + 1), doUpdatePartFrom(1), doUpdateUnitPartTo()).map { Unit }
        else
            Observable.just(Unit)

    fun updateUnitTo(v: Int): Observable<Unit> =
        doUpdateUnitTo(v, false).flatMap {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Observable.just(Unit)
        }

    fun updatePartTo(v: Int): Observable<Unit> =
        doUpdatePartTo(v, false).flatMap {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Observable.just(Unit)
        }

    private fun doUpdateUnitPartFrom(): Observable<Unit> =
        Observables.zip(doUpdateUnitFrom(usunitto), doUpdatePartFrom(uspartto)).map { Unit }

    private fun doUpdateUnitPartTo(): Observable<Unit> =
        Observables.zip(doUpdateUnitTo(usunitfrom), doUpdatePartTo(uspartfrom)).map { Unit }

    private fun doUpdateSingleUnit(): Observable<Unit> =
        Observables.zip(doUpdateUnitTo(usunitfrom), doUpdatePartFrom(1), doUpdatePartTo(partCount)).map { Unit }

    private fun doUpdateUnitFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitfrom == v) return Observable.just(Unit)
        usunitfrom = v
        return userSettingService.update(INFO_USUNITFROM, usunitfrom)
            .applyIO()
            .map { settingsListener?.onUpdateUnitFrom() }
    }

    private fun doUpdatePartFrom(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartfrom == v) return Observable.just(Unit)
        uspartfrom = v
        return userSettingService.update(INFO_USPARTFROM, uspartfrom)
            .applyIO()
            .map { settingsListener?.onUpdatePartFrom() }
    }

    private fun doUpdateUnitTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && usunitto == v) return Observable.just(Unit)
        usunitto = v
        return userSettingService.update(INFO_USUNITTO, usunitto)
            .applyIO()
            .map { settingsListener?.onUpdateUnitTo() }
    }

    private fun doUpdatePartTo(v: Int, check: Boolean = true): Observable<Unit> {
        if (check && uspartto == v) return Observable.just(Unit)
        uspartto = v
        return userSettingService.update(INFO_USPARTTO, uspartto)
            .applyIO()
            .map { settingsListener?.onUpdatePartTo() }
    }

    fun getHtml(url: String): Observable<String> =
        htmlService.getHtml(url)

    fun getNote(word: String): Observable<String> {
        val dictNote = selectedDictNote ?: return Observable.empty()
        val url = dictNote.urlString(word, lstAutoCorrect)
        return getHtml(url).map {
            Log.d("", it)
            extractTextFrom(it, dictNote.transform, "") { text, _ -> text }
        }
    }

    fun getNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
        val dictNote = selectedDictNote ?: return
        var i = 0
        var subscription: Disposable? = null
        subscription = Observable.interval(dictNote.wait.toLong(), TimeUnit.MILLISECONDS, Schedulers.io()).subscribe {
            while (i < wordCount && !isNoteEmpty(i))
                i++
            if (i > wordCount) {
                allComplete()
                subscription?.dispose()
            } else {
                if (i < wordCount)
                    getOne(i)
                i++
            }
        }
        compositeDisposable.add(subscription)
    }

    fun clearNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
        var i = 0
        while (i < wordCount) {
            while (i < wordCount && !isNoteEmpty(i))
                i++
            if (i < wordCount)
                getOne(i)
            i++
        }
        allComplete()
    }
}

interface SettingsListener {
    fun onGetData() {}
    fun onUpdateLang() {}
    fun onUpdateTextbook() {}
    fun onUpdateDictReference() {}
    fun onUpdateDictNote() {}
    fun onUpdateDictTranslation() {}
    fun onUpdateVoice() {}
    fun onUpdateUnitFrom() {}
    fun onUpdatePartFrom() {}
    fun onUpdateUnitTo() {}
    fun onUpdatePartTo() {}
}
