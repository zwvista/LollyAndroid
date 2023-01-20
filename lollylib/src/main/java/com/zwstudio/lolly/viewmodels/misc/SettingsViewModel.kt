package com.zwstudio.lolly.viewmodels.misc

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.tts
import com.zwstudio.lolly.models.misc.*
import com.zwstudio.lolly.services.misc.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

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

    var lstLanguages_ = MutableStateFlow(listOf<MLanguage>())
    var lstLanguages get() = lstLanguages_.value; set(v) { lstLanguages_.value = v }
    val selectedLangIndex_= MutableStateFlow(-1)
    var selectedLangIndex get() = selectedLangIndex_.value; set(v) { selectedLangIndex_.value = v }
    val selectedLang get() = lstLanguages.getOrElse(selectedLangIndex) { MLanguage() }

    var lstVoices_ = MutableStateFlow(listOf<MVoice>())
    var lstVoices get() = lstVoices_.value; set(v) { lstVoices_.value = v }
    val selectedVoiceIndex_= MutableStateFlow(-1)
    var selectedVoiceIndex get() = selectedVoiceIndex_.value; set(v) { selectedVoiceIndex_.value = v }
    val selectedVoice get() = lstVoices.getOrElse(selectedVoiceIndex) { MVoice() }

    var lstTextbooks_ = MutableStateFlow(listOf<MTextbook>())
    var lstTextbooks get() = lstTextbooks_.value; set(v) { lstTextbooks_.value = v }
    val selectedTextbookIndex_= MutableStateFlow(-1)
    var selectedTextbookIndex get() = selectedTextbookIndex_.value; set(v) { selectedTextbookIndex_.value = v }
    val selectedTextbook get() = lstTextbooks.getOrElse(selectedTextbookIndex) { MTextbook() }
    val lstTextbookFilters_ = MutableStateFlow(listOf<MSelectItem>())
    var lstTextbookFilters get() = lstTextbookFilters_.value; set(v) { lstTextbookFilters_.value = v }

    var lstDictsReference_ = MutableStateFlow(listOf<MDictionary>())
    var lstDictsReference get() = lstDictsReference_.value; set(v) { lstDictsReference_.value = v }
    val selectedDictReferenceIndex_= MutableStateFlow(-1)
    var selectedDictReferenceIndex get() = selectedDictReferenceIndex_.value; set(v) { selectedDictReferenceIndex_.value = v }
    val selectedDictReference get() = lstDictsReference.getOrElse(selectedDictReferenceIndex) { MDictionary() }

    var lstDictsNote_ = MutableStateFlow(listOf<MDictionary>())
    var lstDictsNote get() = lstDictsNote_.value; set(v) { lstDictsNote_.value = v }
    val selectedDictNoteIndex_= MutableStateFlow(-1)
    var selectedDictNoteIndex get() = selectedDictNoteIndex_.value; set(v) { selectedDictNoteIndex_.value = v }
    val selectedDictNote get() = lstDictsNote.getOrElse(selectedDictNoteIndex) { MDictionary() }

    var lstDictsTranslation_ = MutableStateFlow(listOf<MDictionary>())
    var lstDictsTranslation get() = lstDictsTranslation_.value; set(v) { lstDictsTranslation_.value = v }
    val selectedDictTranslationIndex_= MutableStateFlow(-1)
    var selectedDictTranslationIndex get() = selectedDictTranslationIndex_.value; set(v) { selectedDictTranslationIndex_.value = v }
    val selectedDictTranslation get() = lstDictsTranslation.getOrElse(selectedDictTranslationIndex) { MDictionary() }

    var lstUnits_ = MutableStateFlow(listOf<MSelectItem>())
    var lstUnits get() = lstUnits_.value; set(v) { lstUnits_.value = v }
    val unitCount: Int
        get() = lstUnits.size
    var lstParts_ = MutableStateFlow(listOf<MSelectItem>())
    var lstParts get() = lstParts_.value; set(v) { lstParts_.value = v }
    val partCount: Int
        get() = lstParts.size
    val isSinglePart: Boolean
        get() = partCount == 1

    var lstAutoCorrect = listOf<MAutoCorrect>()

    val selectedUnitFromIndex_ = MutableStateFlow(-1)
    var selectedUnitFromIndex get() = selectedUnitFromIndex_.value; set(v) { selectedUnitFromIndex_.value = v }
    val selectedPartFromIndex_ = MutableStateFlow(-1)
    var selectedPartFromIndex get() = selectedPartFromIndex_.value; set(v) { selectedPartFromIndex_.value = v }
    val selectedUnitToIndex_ = MutableStateFlow(-1)
    var selectedUnitToIndex get() = selectedUnitToIndex_.value; set(v) { selectedUnitToIndex_.value = v }
    val selectedPartToIndex_ = MutableStateFlow(-1)
    var selectedPartToIndex get() = selectedPartToIndex_.value; set(v) { selectedPartToIndex_.value = v }
    val toTypeIndex_ = MutableStateFlow(UnitPartToType.To.ordinal)
    var toType get() = UnitPartToType.values()[toTypeIndex_.value]; set(v) { toTypeIndex_.value = v.ordinal }

    companion object {
        val lstToTypes = UnitPartToType.values().map { v -> MSelectItem(v.ordinal, v.toString()) }
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

    var initialized = MutableStateFlow(false)

    val unitToEnabled = MutableStateFlow(false)
    val partToEnabled = MutableStateFlow(false)
    val previousEnabled = MutableStateFlow(false)
    val nextEnabled = MutableStateFlow(false)
    val partFromEnabled = MutableStateFlow(false)

    init {
        fun MutableStateFlow<Int>.onChange(action: suspend (Int) -> Unit) =
            this.filter { it != -1 }.onEach(action).launchIn(viewModelScope)

        selectedLangIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedLang.id
                val dirty = uslang != newVal
                uslang = newVal
                selectedDictReferenceIndex = -1
                selectedDictNoteIndex = -1
                selectedDictTranslationIndex = -1
                selectedTextbookIndex = -1
                selectedVoiceIndex = -1
                toType = UnitPartToType.To
                INFO_USTEXTBOOK = getUSInfo(MUSMapping.NAME_USTEXTBOOK)
                INFO_USDICTREFERENCE = getUSInfo(MUSMapping.NAME_USDICTREFERENCE)
                INFO_USDICTNOTE = getUSInfo(MUSMapping.NAME_USDICTNOTE)
                INFO_USDICTTRANSLATION = getUSInfo(MUSMapping.NAME_USDICTTRANSLATION)
                INFO_USVOICE = getUSInfo(MUSMapping.NAME_USVOICE)
                val res1 = async { dictionaryService.getDictsReferenceByLang(uslang) }
                val res2 = async { dictionaryService.getDictsNoteByLang(uslang) }
                val res3 = async { dictionaryService.getDictsTranslationByLang(uslang) }
                val res4 = async { textbookService.getDataByLang(uslang) }
                val res5 = async { autoCorrectService.getDataByLang(uslang) }
                val res6 = async { voiceService.getDataByLang(uslang) }
                val res7 = async { if (dirty) userSettingService.update(INFO_USLANG, uslang) }
                lstDictsReference = res1.await()
                lstDictsNote = res2.await()
                lstDictsTranslation = res3.await()
                lstTextbooks = res4.await()
                lstTextbookFilters = listOf(MSelectItem(0, "All Textbooks")) + lstTextbooks.map { MSelectItem(it.id, it.textbookname) }
                lstAutoCorrect = res5.await()
                lstVoices = res6.await()
                res7.await()
                selectedVoiceIndex = 0.coerceAtLeast(lstVoices.indexOfFirst { it.id == usvoice })
                selectedDictReferenceIndex = 0.coerceAtLeast(lstDictsReference.indexOfFirst { it.dictid.toString() == usdictreference })
                selectedDictNoteIndex = 0.coerceAtLeast(lstDictsNote.indexOfFirst { it.dictid == usdictnote })
                selectedDictTranslationIndex = 0.coerceAtLeast(lstDictsTranslation.indexOfFirst { it.dictid == usdicttranslation })
                selectedTextbookIndex = 0.coerceAtLeast(lstTextbooks.indexOfFirst { it.id == ustextbook })
            }
        }

        selectedVoiceIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedVoice.id
                val dirty = usvoice != newVal
                usvoice = newVal
                val locale = Locale.getAvailableLocales().find {
                    "${it.language}_${it.country}" == selectedVoice.voicelang
                }
                if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return@launch
                tts.language = locale
                if (dirty) userSettingService.update(INFO_USVOICE, usvoice)
            }
        }

        selectedDictReferenceIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedDictReference.dictid.toString()
                val dirty = usdictreference != newVal
                usdictreference = newVal
                if (dirty) userSettingService.update(INFO_USDICTREFERENCE, usdictreference)
            }
        }

        selectedDictNoteIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedDictNote.dictid
                val dirty = usdictnote != newVal
                usdictnote = newVal
                if (dirty) userSettingService.update(INFO_USDICTNOTE, usdictnote)
            }
        }

        selectedDictTranslationIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedDictTranslation.dictid
                val dirty = usdicttranslation != newVal
                usdicttranslation = newVal
                if (dirty) userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslation)
            }
        }

        selectedTextbookIndex_.onChange {
            viewModelScope.launch {
                val newVal = selectedTextbook.id
                val dirty = ustextbook != newVal
                ustextbook = newVal
                selectedUnitFromIndex = -1
                selectedPartFromIndex = -1
                selectedUnitToIndex = -1
                selectedPartToIndex = -1
                INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
                INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
                INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
                INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
                lstUnits = selectedTextbook.lstUnits
                lstParts = selectedTextbook.lstParts
                selectedUnitFromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
                selectedPartFromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
                selectedUnitToIndex = lstUnits.indexOfFirst { it.value == usunitto }
                selectedPartToIndex = lstParts.indexOfFirst { it.value == uspartto }
                toType = if (isSingleUnit) UnitPartToType.Unit else if (isSingleUnitPart) UnitPartToType.Part else UnitPartToType.To
                if (dirty) userSettingService.update(INFO_USTEXTBOOK, ustextbook)
            }
        }

        selectedUnitFromIndex_.onChange {
            viewModelScope.launch {
                doUpdateUnitFrom(lstUnits[it].value)
                if (toType == UnitPartToType.Unit)
                    doUpdateSingleUnit()
                else if (toType == UnitPartToType.Part || isInvalidUnitPart)
                    doUpdateUnitPartTo()
            }
        }

        selectedPartFromIndex_.onChange {
            viewModelScope.launch {
                doUpdatePartFrom(lstParts[it].value)
                if (toType == UnitPartToType.Part || isInvalidUnitPart)
                    doUpdateUnitPartTo()
            }
        }

        toTypeIndex_.onEach {
            val b = it == UnitPartToType.To.ordinal
            unitToEnabled.value = b
            partToEnabled.value = b && !isSinglePart
            previousEnabled.value = !b
            nextEnabled.value = !b
            partFromEnabled.value = it != UnitPartToType.Unit.ordinal && !isSinglePart

            viewModelScope.launch {
                if (toType == UnitPartToType.Unit)
                    doUpdateSingleUnit()
                else if (toType == UnitPartToType.Part)
                    doUpdateUnitPartTo()
            }
        }.launchIn(viewModelScope)

        selectedUnitToIndex_.onChange {
            viewModelScope.launch {
                doUpdateUnitTo(lstUnits[it].value)
                if (isInvalidUnitPart)
                    doUpdateUnitPartFrom()
            }
        }

        selectedPartToIndex_.onChange {
            viewModelScope.launch {
                doUpdatePartTo(lstParts[it].value)
                if (isInvalidUnitPart)
                    doUpdateUnitPartFrom()
            }
        }
    }

    fun getData() = viewModelScope.launch {
        val res1 = async { languageService.getData() }
        val res2 = async { usMappingService.getData() }
        val res3 = async { userSettingService.getData() }
        lstLanguages = res1.await()
        lstUSMappings = res2.await()
        lstUserSettings = res3.await()
        INFO_USLANG = getUSInfo(MUSMapping.NAME_USLANG)
        selectedLangIndex = 0.coerceAtLeast(lstLanguages.indexOfFirst { it.id == uslang })
    }

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun toggleUnitPart(part: Int) = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            toType = UnitPartToType.Part
            val res1 = async { doUpdatePartFrom(part) }
            val res2 = async { doUpdateUnitPartTo() }
            res1.await(); res2.await()
        } else if (toType == UnitPartToType.Part) {
            toType = UnitPartToType.Unit
            doUpdateSingleUnit()
        }
    }

    fun previousUnitPart() = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            if (usunitfrom > 1) {
                val res1 = async { doUpdateUnitFrom(usunitfrom - 1) }
                val res2 = async { doUpdateUnitTo(usunitfrom) }
                res1.await(); res2.await()
            }
        } else if (uspartfrom > 1) {
            val res1 = async { doUpdatePartFrom(uspartfrom - 1) }
            val res2 = async { doUpdateUnitPartTo() }
            res1.await(); res2.await()
        } else if (usunitfrom > 1) {
            val res1 = async { doUpdateUnitFrom(usunitfrom - 1) }
            val res2 = async { doUpdatePartFrom(partCount) }
            val res3 = async { doUpdateUnitPartTo() }
            res1.await(); res2.await(); res3.await()
        }
    }

    fun nextUnitPart() = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            if (usunitfrom < unitCount) {
                val res1 = async { doUpdateUnitFrom(usunitfrom + 1) }
                val res2 = async { doUpdateUnitTo(usunitfrom) }
                res1.await(); res2.await()
            }
        } else if (uspartfrom < partCount) {
            val res1 = async { doUpdatePartFrom(uspartfrom + 1) }
            val res2 = async { doUpdateUnitPartTo() }
            res1.await(); res2.await()
        } else if (usunitfrom < unitCount) {
            val res1 = async { doUpdateUnitFrom(usunitfrom + 1) }
            val res2 = async { doUpdatePartFrom(1) }
            val res3 = async { doUpdateUnitPartTo() }
            res1.await(); res2.await(); res3.await()
        }
    }

    private suspend fun doUpdateUnitPartFrom() {
        coroutineScope {
            val res1 = async { doUpdateUnitFrom(usunitto) }
            val res2 = async { doUpdatePartFrom(uspartto) }
            res1.await(); res2.await()
        }
    }

    private suspend fun doUpdateUnitPartTo() {
        coroutineScope {
            val res1 = async { doUpdateUnitTo(usunitfrom) }
            val res2 = async { doUpdatePartTo(uspartfrom) }
            res1.await(); res2.await()
        }
    }

    private suspend fun doUpdateSingleUnit() {
        coroutineScope {
            val res1 = async { doUpdateUnitTo(usunitfrom) }
            val res2 = async { doUpdatePartFrom(1) }
            val res3 = async { doUpdatePartTo(partCount) }
            res1.await(); res2.await(); res3.await()
        }
    }

    private suspend fun doUpdateUnitFrom(v: Int) {
        val dirty = usunitfrom != v
        usunitfrom = v
        if (dirty) userSettingService.update(INFO_USUNITFROM, usunitfrom)
        selectedUnitFromIndex = lstUnits.indexOfFirst { it.value == usunitfrom }
    }

    private suspend fun doUpdatePartFrom(v: Int) {
        val dirty = uspartfrom != v
        uspartfrom = v
        if (dirty) userSettingService.update(INFO_USPARTFROM, uspartfrom)
        selectedPartFromIndex = lstParts.indexOfFirst { it.value == uspartfrom }
    }

    private suspend fun doUpdateUnitTo(v: Int) {
        val dirty = usunitto != v
        usunitto = v
        if (dirty) userSettingService.update(INFO_USUNITTO, usunitto)
        selectedUnitToIndex = lstUnits.indexOfFirst { it.value == usunitto }
    }

    private suspend fun doUpdatePartTo(v: Int) {
        val dirty = uspartto != v
        uspartto = v
        if (dirty) userSettingService.update(INFO_USPARTTO, uspartto)
        selectedPartToIndex = lstParts.indexOfFirst { it.value == uspartto }
    }

    suspend fun getHtml(url: String): String =
        htmlService.getHtml(url)

    suspend fun getNote(word: String): String {
        val dictNote = selectedDictNote
        val url = dictNote.urlString(word, lstAutoCorrect)
        val html = getHtml(url)
        Log.d("API Result", html)
        return extractTextFrom(html, dictNote.transform, "") { text, _ -> text }
    }

    suspend fun getNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
        val dictNote = selectedDictNote
        var i = 0
        while (i <= wordCount) {
            while (i < wordCount && !isNoteEmpty(i))
                i++
            if (i < wordCount)
                getOne(i)
            i++
            delay(dictNote.wait.toLong())
        }
        allComplete()
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
