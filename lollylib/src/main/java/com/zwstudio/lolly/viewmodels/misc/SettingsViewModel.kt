package com.zwstudio.lolly.viewmodels.misc

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.tts
import com.zwstudio.lolly.models.misc.*
import com.zwstudio.lolly.services.misc.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.TimeUnit

enum class UnitPartToType {
    Unit, Part, To
}

class SettingsViewModel : ViewModel(), KoinComponent {

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
    private var INFO_USDICTTRANSLATION = MUserSettingInfo()
    var usdicttranslation: Int
        get() = (getUSValue(INFO_USDICTTRANSLATION) ?: "0").toInt()
        set(value) = setUSValue(INFO_USDICTTRANSLATION, value.toString())
    private var INFO_USVOICE = MUserSettingInfo()
    var usvoice: Int
        get() = (getUSValue(INFO_USVOICE) ?: "0").toInt()
        set(value) = setUSValue(INFO_USVOICE, value.toString())
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
    val selectedUnitFromIndex_ = MutableLiveData(0)
    var selectedUnitFromIndex get() = selectedUnitFromIndex_.value!!; set(v) { selectedUnitFromIndex_.value = v }
    val selectedPartFromIndex_ = MutableLiveData(0)
    var selectedPartFromIndex get() = selectedPartFromIndex_.value!!; set(v) { selectedPartFromIndex_.value = v }
    val selectedUnitToIndex_ = MutableLiveData(0)
    var selectedUnitToIndex get() = selectedUnitToIndex_.value!!; set(v) { selectedUnitToIndex_.value = v }
    val selectedPartToIndex_ = MutableLiveData(0)
    var selectedPartToIndex get() = selectedPartToIndex_.value!!; set(v) { selectedPartToIndex_.value = v }
    val toTypeIndex_ = MutableLiveData(0)
    var toType get() = UnitPartToType.values()[toTypeIndex_.value!!]; set(v) { toTypeIndex_.value = v.ordinal }

    companion object {
        val lstScopeWordFilters = listOf("Word", "Note").mapIndexed { index, s -> MSelectItem(index, s) }
        val lstScopePhraseFilters = listOf("Phrase", "Translation").mapIndexed { index, s -> MSelectItem(index, s) }
        val lstScopePatternFilters = listOf("Pattern", "Note", "Tags").mapIndexed { index, s -> MSelectItem(index, s) }
        val lstReviewModes = ReviewMode.values().mapIndexed { index, s -> MSelectItem(index, s.toString()) }
        const val zeroNote = "O"
    }

    private val languageService by inject<LanguageService>()
    private val usMappingService by inject<USMappingService>()
    private val userSettingService by inject<UserSettingService>()
    private val dictionaryService by inject<DictionaryService>()
    private val textbookService by inject<TextbookService>()
    private val autoCorrectService by inject<AutoCorrectService>()
    private val voiceService by inject<VoiceService>()
    private val htmlService by inject<HtmlService>()
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

    val unitToEnabled = MutableLiveData(false)
    val partToEnabled = MutableLiveData(false)
    val previousEnabled = MutableLiveData(false)
    val nextEnabled = MutableLiveData(false)
    val partFromEnabled = MutableLiveData(false)

    fun addObservers(lifecycleOwner: LifecycleOwner) {
        selectedLangIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateLang().subscribe())
        }
        selectedVoiceIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateVoice().subscribe())
        }
        selectedDictReferenceIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateDictReference().subscribe())
        }
        selectedDictNoteIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateDictNote().subscribe())
        }
        selectedDictTranslationIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateDictTranslation().subscribe())
        }
        selectedTextbookIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateTextbook().subscribe())
        }
        selectedUnitFromIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateUnitFrom(lstUnits[it].value).subscribe())
        }
        selectedPartFromIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updatePartFrom(it).subscribe())
        }
        toTypeIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            val b = it == 2
            unitToEnabled.value = b
            partToEnabled.value = b && !isSinglePart
            previousEnabled.value = !b
            nextEnabled.value = !b
            partFromEnabled.value = it != 0 && !isSinglePart
            if (!busy)
                compositeDisposable.add(updateToType(it).subscribe())
        }
        selectedUnitToIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updateUnitTo(lstUnits[it].value).subscribe())
        }
        selectedPartToIndex_.distinctUntilChanged().observe(lifecycleOwner) {
            if (!busy)
                compositeDisposable.add(updatePartTo(lstParts[it].value).subscribe())
        }
    }

    fun getData(): Completable {
        busy = true
        return Single.zip(languageService.getData(),
            usMappingService.getData(),
            userSettingService.getData()) { res1, res2, res3 ->
            lstLanguages = res1
            lstUSMappings = res2
            lstUserSettings = res3
            INFO_USLANG = getUSInfo(MUSMapping.NAME_USLANG)
        }.applyIO().flatMapCompletable {
            selectedLangIndex = 0.coerceAtLeast(lstLanguages.indexOfFirst { it.id == uslang })
            settingsListener?.onGetData()
            updateLang()
        }.doAfterTerminate { busy = false }
    }

    fun updateLang(): Completable {
        if (lstLanguages.isEmpty()) return Completable.complete()
        busy = true
        val newVal = selectedLang.id
        val dirty = uslang != newVal
        uslang = newVal
        INFO_USTEXTBOOK = getUSInfo(MUSMapping.NAME_USTEXTBOOK)
        INFO_USDICTREFERENCE = getUSInfo(MUSMapping.NAME_USDICTREFERENCE)
        INFO_USDICTNOTE = getUSInfo(MUSMapping.NAME_USDICTNOTE)
        INFO_USDICTTRANSLATION = getUSInfo(MUSMapping.NAME_USDICTTRANSLATION)
        INFO_USVOICE = getUSInfo(MUSMapping.NAME_USVOICE)
        return Single.zip(dictionaryService.getDictsReferenceByLang(uslang),
            dictionaryService.getDictsNoteByLang(uslang),
            dictionaryService.getDictsTranslationByLang(uslang),
            textbookService.getDataByLang(uslang),
            autoCorrectService.getDataByLang(uslang),
            voiceService.getDataByLang(uslang),
            if (dirty) userSettingService.update(INFO_USLANG, uslang).toSingle { 0 } else Single.just(0)) { res1, res2, res3, res4, res5, res6, _ ->
            lstDictsReference = res1
            lstDictsNote = res2
            lstDictsTranslation = res3
            lstTextbooks = res4
            lstTextbookFilters = listOf(MSelectItem(0, "All Textbooks")) + lstTextbooks.map { MSelectItem(it.id, it.textbookname) }
            lstAutoCorrect = res5
            lstVoices = res6
        }.applyIO().flatMapCompletable {
            selectedVoiceIndex = 0.coerceAtLeast(lstVoices.indexOfFirst { it.id == usvoice })
            selectedDictReferenceIndex = 0.coerceAtLeast(lstDictsReference.indexOfFirst { it.dictid.toString() == usdictreference })
            selectedDictNoteIndex = 0.coerceAtLeast(lstDictsNote.indexOfFirst { it.dictid == usdictnote })
            selectedDictTranslationIndex = 0.coerceAtLeast(lstDictsTranslation.indexOfFirst { it.dictid == usdicttranslation })
            selectedTextbookIndex = 0.coerceAtLeast(lstTextbooks.indexOfFirst { it.id == ustextbook })
            settingsListener?.onUpdateLang()
            Completable.mergeArray(updateVoice(), updateDictReference(), updateDictNote(), updateDictTranslation(), updateTextbook())
        }.doAfterTerminate { busy = false }
    }

    fun updateTextbook(): Completable {
        if (lstTextbooks.isEmpty()) return Completable.complete()
        busy = true
        val newVal = selectedTextbook.id
        val dirty = ustextbook != newVal
        ustextbook = newVal
        INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
        INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
        INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
        INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
        return (if (dirty) userSettingService.update(INFO_USTEXTBOOK, ustextbook) else Completable.complete())
            .applyIO().doAfterTerminate {
                selectedUnitFromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
                selectedPartFromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
                selectedUnitToIndex = lstUnits.indexOfFirst { it.value == usunitto }
                selectedPartToIndex = lstParts.indexOfFirst { it.value == uspartto }
                toType = if (isSingleUnit) UnitPartToType.Unit else if (isSingleUnitPart) UnitPartToType.Part else UnitPartToType.To
                settingsListener?.onUpdateTextbook()
                busy = false
            }
    }

    fun updateDictReference(): Completable {
        if (lstDictsReference.isEmpty()) return Completable.complete()
        val newVal = selectedDictReference.dictid.toString()
        val dirty = usdictreference != newVal
        usdictreference = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTREFERENCE, usdictreference) else Completable.complete())
            .applyIO().doAfterTerminate { settingsListener?.onUpdateDictReference() }
    }

    fun updateDictNote(): Completable {
        if (lstDictsNote.isEmpty()) return Completable.complete()
        val newVal = selectedDictNote?.dictid ?: 0
        val dirty = usdictnote != newVal
        usdictnote = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTNOTE, usdictnote) else Completable.complete())
            .applyIO().doAfterTerminate { settingsListener?.onUpdateDictNote() }
    }

    fun updateDictTranslation(): Completable {
        if (lstDictsTranslation.isEmpty()) return Completable.complete()
        val newVal = selectedDictTranslation?.dictid ?: 0
        val dirty = usdicttranslation != newVal
        usdicttranslation = newVal
        return (if (dirty) userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslation) else Completable.complete())
            .applyIO().doAfterTerminate { settingsListener?.onUpdateDictTranslation() }
    }

    fun updateVoice(): Completable {
        if (lstVoices.isEmpty()) return Completable.complete()
        val newVal = selectedVoice?.id ?: 0
        val dirty = usvoice != newVal
        usvoice = newVal
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return Completable.complete()
        tts.language = locale
        return (if (dirty) userSettingService.update(INFO_USVOICE, usvoice) else Completable.complete())
            .applyIO().doAfterTerminate { settingsListener?.onUpdateVoice() }
    }

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun updateUnitFrom(v: Int): Completable =
        doUpdateUnitFrom(v).andThen {
            if (toType == UnitPartToType.Unit)
                doUpdateSingleUnit()
            else if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Completable.complete()
        }

    fun updatePartFrom(v: Int): Completable =
        doUpdatePartFrom(v).andThen {
            if (toType == UnitPartToType.Part || isInvalidUnitPart)
                doUpdateUnitPartTo()
            else
                Completable.complete()
        }

    fun updateToType(v: Int): Completable {
        toType = UnitPartToType.values()[v]
        return if (toType == UnitPartToType.Unit)
            doUpdateSingleUnit()
        else if (toType == UnitPartToType.Part)
            doUpdateUnitPartTo()
        else
            Completable.complete()
    }

    fun toggleUnitPart(part: Int): Completable =
        if (toType == UnitPartToType.Unit) {
            toType = UnitPartToType.Part
            Completable.mergeArray(doUpdatePartFrom(part), doUpdateUnitPartTo())
        } else if (toType == UnitPartToType.Part) {
            toType = UnitPartToType.Unit
            doUpdateSingleUnit()
        } else
            Completable.complete()

    fun previousUnitPart(): Completable =
        if (toType == UnitPartToType.Unit)
            if (usunitfrom > 1)
                Completable.mergeArray(doUpdateUnitFrom(usunitfrom - 1), doUpdateUnitTo(usunitfrom))
            else
                Completable.complete()
        else if (uspartfrom > 1)
            Completable.mergeArray(doUpdatePartFrom(uspartfrom - 1), doUpdateUnitPartTo())
        else if (usunitfrom > 1)
            Completable.mergeArray(doUpdateUnitFrom(usunitfrom - 1), doUpdatePartFrom(partCount), doUpdateUnitPartTo())
        else
            Completable.complete()

    fun nextUnitPart(): Completable =
        if (toType == UnitPartToType.Unit)
            if (usunitfrom < unitCount)
                Completable.mergeArray(doUpdateUnitFrom(usunitfrom + 1), doUpdateUnitTo(usunitfrom))
            else
                Completable.complete()
        else if (uspartfrom < partCount)
            Completable.mergeArray(doUpdatePartFrom(uspartfrom + 1), doUpdateUnitPartTo())
        else if (usunitfrom < unitCount)
            Completable.mergeArray(doUpdateUnitFrom(usunitfrom + 1), doUpdatePartFrom(1), doUpdateUnitPartTo())
        else
            Completable.complete()

    fun updateUnitTo(v: Int): Completable =
        doUpdateUnitTo(v).andThen {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Completable.complete()
        }

    fun updatePartTo(v: Int): Completable =
        doUpdatePartTo(v).andThen {
            if (isInvalidUnitPart)
                doUpdateUnitPartFrom()
            else
                Completable.complete()
        }

    private fun doUpdateUnitPartFrom(): Completable =
        Completable.mergeArray(doUpdateUnitFrom(usunitto), doUpdatePartFrom(uspartto))

    private fun doUpdateUnitPartTo(): Completable =
        Completable.mergeArray(doUpdateUnitTo(usunitfrom), doUpdatePartTo(uspartfrom))

    private fun doUpdateSingleUnit(): Completable =
        Completable.mergeArray(doUpdateUnitTo(usunitfrom), doUpdatePartFrom(1), doUpdatePartTo(partCount))

    private fun doUpdateUnitFrom(v: Int): Completable {
        val dirty = usunitfrom != v
        usunitfrom = v
        return (if (dirty) userSettingService.update(INFO_USUNITFROM, usunitfrom) else Completable.complete())
            .applyIO().doAfterTerminate {
                selectedUnitFromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
                settingsListener?.onUpdateUnitFrom()
            }
    }

    private fun doUpdatePartFrom(v: Int): Completable {
        val dirty = uspartfrom != v
        uspartfrom = v
        return (if (dirty) userSettingService.update(INFO_USPARTFROM, uspartfrom) else Completable.complete())
            .applyIO().doAfterTerminate {
                selectedPartFromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
                settingsListener?.onUpdatePartFrom()
            }
    }

    private fun doUpdateUnitTo(v: Int): Completable {
        val dirty = usunitto != v
        usunitto = v
        return (if (dirty) userSettingService.update(INFO_USUNITTO, usunitto) else Completable.complete())
            .applyIO().doAfterTerminate {
                selectedUnitToIndex = lstUnits.indexOfFirst { it.value == usunitto }
                settingsListener?.onUpdateUnitTo()
            }
    }

    private fun doUpdatePartTo(v: Int): Completable {
        val dirty = uspartto != v
        uspartto = v
        return (if (dirty) userSettingService.update(INFO_USPARTTO, uspartto) else Completable.complete())
            .applyIO().doAfterTerminate {
                selectedPartToIndex = lstParts.indexOfFirst { it.value == uspartto }
                settingsListener?.onUpdatePartTo()
            }
    }

    fun getHtml(url: String): Single<String> =
        htmlService.getHtml(url)

    fun getNote(word: String): Single<String> {
        val dictNote = selectedDictNote ?: return Single.just("")
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
        compositeDisposable.add(subscription!!)
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
