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
    val selectedLangIndex_= MutableLiveData(0)
    var selectedLangIndex get() = selectedLangIndex_.value!!; set(v) { selectedLangIndex_.value = v }
    val selectedLang get() = lstLanguages[selectedLangIndex]

    var lstVoices = listOf<MVoice>()
    val selectedVoiceIndex_= MutableLiveData(0)
    var selectedVoiceIndex get() = selectedVoiceIndex_.value!!; set(v) { selectedVoiceIndex_.value = v }
    val selectedVoice get() = lstVoices.getOrNull(selectedVoiceIndex)

    var lstTextbooks = listOf<MTextbook>()
    val selectedTextbookIndex_= MutableLiveData(0)
    var selectedTextbookIndex get() = selectedTextbookIndex_.value!!; set(v) { selectedTextbookIndex_.value = v }
    val selectedTextbook get() = lstTextbooks[selectedTextbookIndex]
    var lstTextbookFilters = listOf<MSelectItem>()

    var lstDictsReference = listOf<MDictionary>()
    val selectedDictReferenceIndex_= MutableLiveData(0)
    var selectedDictReferenceIndex get() = selectedDictReferenceIndex_.value!!; set(v) { selectedDictReferenceIndex_.value = v }
    val selectedDictReference get() = lstDictsReference[selectedDictReferenceIndex]

    var lstDictsNote = listOf<MDictionary>()
    val selectedDictNoteIndex_= MutableLiveData(0)
    var selectedDictNoteIndex get() = selectedDictNoteIndex_.value!!; set(v) { selectedDictNoteIndex_.value = v }
    val selectedDictNote get() = lstDictsNote.getOrNull(selectedDictNoteIndex)

    var lstDictsTranslation = listOf<MDictionary>()
    val selectedDictTranslationIndex_= MutableLiveData(0)
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
    val unitfromIndex_ = MutableLiveData(0)
    var unitfromIndex get() = unitfromIndex_.value!!; set(v) { unitfromIndex_.value = v }
    val partfromIndex_ = MutableLiveData(0)
    var partfromIndex get() = partfromIndex_.value!!; set(v) { partfromIndex_.value = v }
    val unittoIndex_ = MutableLiveData(0)
    var unittoIndex get() = unittoIndex_.value!!; set(v) { unittoIndex_.value = v }
    val parttoIndex_ = MutableLiveData(0)
    var parttoIndex get() = parttoIndex_.value!!; set(v) { parttoIndex_.value = v }
    val toTypeIndex_ = MutableLiveData(0)
    var toType get() = UnitPartToType.values()[toTypeIndex_.value!!]; set(v) { toTypeIndex_.value = v.ordinal }

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

    var busy = false
    var settingsListener: SettingsListener? = null
    fun getData(): Observable<Unit> {
        busy = true
        return Observable.zip(languageService.getData(),
            usMappingService.getData(),
            userSettingService.getData()) { res1, res2, res3 ->
            lstLanguages = res1
            lstUSMappings = res2
            lstUserSettings = res3
            INFO_USLANG = getUSInfo(MUSMapping.NAME_USLANG)
        }.applyIO().flatMap {
            selectedLangIndex = 0.coerceAtLeast(lstLanguages.indexOfFirst { it.id == uslang })
            settingsListener?.onGetData()
            updateLang()
        }.map { busy = false }
    }

    fun updateLang(): Observable<Unit> {
        if (lstLanguages.isEmpty()) return Observable.empty()
        busy = true
        val newVal = selectedLang.id
        val dirty = uslang != newVal
        uslang = newVal
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
            if (dirty) userSettingService.update(INFO_USLANG, uslang) else Observable.just(Unit)) { res1, res2, res3, res4, res5, res6, _ ->
            lstDictsReference = res1
            lstDictsNote = res2
            lstDictsTranslation = res3
            lstTextbooks = res4
            lstTextbookFilters = listOf(MSelectItem(0, "All Textbooks")) + lstTextbooks.map { MSelectItem(it.id, it.textbookname) }
            lstAutoCorrect = res5
            lstVoices = res6
        }.applyIO().flatMap {
            selectedVoiceIndex = 0.coerceAtLeast(lstVoices.indexOfFirst { it.id == usvoice })
            selectedDictReferenceIndex = 0.coerceAtLeast(lstDictsReference.indexOfFirst { it.dictid.toString() == usdictreference })
            selectedDictNoteIndex = 0.coerceAtLeast(lstDictsNote.indexOfFirst { it.dictid == usdictnote })
            selectedDictTranslationIndex = 0.coerceAtLeast(lstDictsTranslation.indexOfFirst { it.dictid == usdicttranslation })
            selectedTextbookIndex = 0.coerceAtLeast(lstTextbooks.indexOfFirst { it.id == ustextbook })
            settingsListener?.onUpdateLang()
            Observable.zip(updateVoice(), updateDictReference(), updateDictNote(), updateDictTranslation(), updateTextbook()) {_,_,_,_,_ ->}
        }.map { busy = false }
    }

    fun updateTextbook(): Observable<Unit> {
        if (lstTextbooks.isEmpty()) return Observable.empty()
        busy = true
        val newVal = selectedTextbook.id
        val dirty = ustextbook != newVal
        ustextbook = newVal
        INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
        INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
        INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
        INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
        return (if (dirty) userSettingService.update(INFO_USTEXTBOOK, ustextbook) else Observable.just(Unit))
            .applyIO().map {
                unitfromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
                partfromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
                unittoIndex = lstUnits.indexOfFirst { it.value == usunitto }
                parttoIndex = lstParts.indexOfFirst { it.value == uspartto }
                toType = if (isSingleUnit) UnitPartToType.Unit else if (isSingleUnitPart) UnitPartToType.Part else UnitPartToType.To
                settingsListener?.onUpdateTextbook()
                busy = false
            }
    }

    fun updateDictReference(): Observable<Unit> {
        if (lstDictsReference.isEmpty()) return Observable.empty()
        val newVal = selectedDictReference.dictid.toString()
        val dirty = usdictreference != newVal
        usdictreference = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTREFERENCE, usdictreference) else Observable.just(Unit))
            .applyIO().map { settingsListener?.onUpdateDictReference() }
    }

    fun updateDictNote(): Observable<Unit> {
        if (lstDictsNote.isEmpty()) return Observable.empty()
        val newVal = selectedDictNote?.dictid ?: 0
        val dirty = usdictnote != newVal
        usdictnote = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTNOTE, usdictnote) else Observable.just(Unit))
            .applyIO().map { settingsListener?.onUpdateDictNote() }
    }

    fun updateDictTranslation(): Observable<Unit> {
        if (lstDictsTranslation.isEmpty()) return Observable.empty()
        val newVal = selectedDictTranslation?.dictid ?: 0
        val dirty = usdicttranslation != newVal
        usdicttranslation = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslation) else Observable.just(Unit))
            .applyIO().map { settingsListener?.onUpdateDictTranslation() }
    }

    fun updateVoice(): Observable<Unit> {
        if (lstVoices.isEmpty()) return Observable.empty()
        val newVal = selectedVoice?.id ?: 0
        val dirty = usvoice != newVal
        usvoice = newVal
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return Observable.empty()
        tts.language = locale
        return (if (dirty) userSettingService.update(INFO_USANDROIDVOICE, usvoice) else Observable.just(Unit))
            .applyIO().map { settingsListener?.onUpdateVoice() }
    }

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun updateUnitFrom(newVal: Int): Observable<Unit> =
        doUpdateUnitFrom(newVal).flatMap {
            if (toType == UnitPartToType.Unit)
                doUpdateSingleUnit()
            else if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.just(Unit)
        }

    fun updatePartFrom(newVal: Int): Observable<Unit> =
        doUpdatePartFrom(newVal).flatMap {
            if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Observable.just(Unit)
        }

    fun updateToType(newVal: Int): Observable<Unit> {
        toType = UnitPartToType.values()[newVal]
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

    fun updateUnitTo(newVal: Int): Observable<Unit> =
        doUpdateUnitTo(newVal).flatMap {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Observable.just(Unit)
        }

    fun updatePartTo(newVal: Int): Observable<Unit> =
        doUpdatePartTo(newVal).flatMap {
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

    private fun doUpdateUnitFrom(newVal: Int): Observable<Unit> {
        val dirty = usunitfrom != newVal
        usunitfrom = newVal
        return (if (dirty) userSettingService.update(INFO_USUNITFROM, usunitfrom) else Observable.just(Unit))
            .applyIO().map {
                unitfromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
                settingsListener?.onUpdateUnitFrom()
            }
    }

    private fun doUpdatePartFrom(newVal: Int): Observable<Unit> {
        val dirty = uspartfrom != newVal
        uspartfrom = newVal
        return (if (dirty) userSettingService.update(INFO_USPARTFROM, uspartfrom) else Observable.just(Unit))
            .applyIO().map {
                partfromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
                settingsListener?.onUpdatePartFrom()
            }
    }

    private fun doUpdateUnitTo(newVal: Int): Observable<Unit> {
        val dirty = usunitto != newVal
        usunitto = newVal
        return (if (dirty) userSettingService.update(INFO_USUNITTO, usunitto) else Observable.just(Unit))
            .applyIO().map {
                unittoIndex = lstUnits.indexOfFirst { it.value == usunitto }
                settingsListener?.onUpdateUnitTo()
            }
    }

    private fun doUpdatePartTo(newVal: Int): Observable<Unit> {
        val dirty = uspartto != newVal
        uspartto = newVal
        return (if (dirty) userSettingService.update(INFO_USPARTTO, uspartto) else Observable.just(Unit))
            .applyIO().map {
                parttoIndex = lstParts.indexOfFirst { it.value == uspartto }
                settingsListener?.onUpdatePartTo()
            }
    }

    fun getHtml(url: String): Observable<String> =
        htmlService.getHtml(url)

    fun getNote(word: String): Observable<String> {
        val dictNote = selectedDictNote ?: return Observable.empty()
        val url = dictNote.urlString(word, lstAutoCorrect)
        return getHtml(url).map {
            Log.d("API Result", it)
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
