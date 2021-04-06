package com.zwstudio.lolly.data.misc

import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.android.tts
import com.zwstudio.lolly.domain.misc.*
import com.zwstudio.lolly.service.misc.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

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

    var busy = false
    var settingsListener: SettingsListener? = null
    fun getData() = viewModelScope.launch {
        busy = true
        // TODO async
        lstLanguages = languageService.getData()
        lstUSMappings = usMappingService.getData()
        lstUserSettings = userSettingService.getDataByUser(GlobalConstants.userid)
        INFO_USLANG = getUSInfo(MUSMapping.NAME_USLANG)
        selectedLangIndex = 0.coerceAtLeast(lstLanguages.indexOfFirst { it.id == uslang })
        settingsListener?.onGetData()
        updateLang()
        busy = false
    }

    fun updateLang() = viewModelScope.launch {
        if (lstLanguages.isEmpty()) return@launch
        busy = true
        val isinit = selectedLang.id == uslang
        uslang = selectedLang.id
        INFO_USTEXTBOOK = getUSInfo(MUSMapping.NAME_USTEXTBOOK)
        INFO_USDICTREFERENCE = getUSInfo(MUSMapping.NAME_USDICTREFERENCE)
        INFO_USDICTNOTE = getUSInfo(MUSMapping.NAME_USDICTNOTE)
        INFO_USDICTSREFERENCE = getUSInfo(MUSMapping.NAME_USDICTSREFERENCE)
        INFO_USDICTTRANSLATION = getUSInfo(MUSMapping.NAME_USDICTTRANSLATION)
        INFO_USANDROIDVOICE = getUSInfo(MUSMapping.NAME_USANDROIDVOICE)
        // TODO async
        lstDictsReference = dictionaryService.getDictsReferenceByLang(uslang)
        lstDictsNote = dictionaryService.getDictsNoteByLang(uslang)
        lstDictsTranslation = dictionaryService.getDictsTranslationByLang(uslang)
        lstTextbooks = textbookService.getDataByLang(uslang)
        lstTextbookFilters = listOf(MSelectItem(0, "All Textbooks")) + lstTextbooks.map { MSelectItem(it.id, it.textbookname) }
        lstAutoCorrect = autoCorrectService.getDataByLang(uslang)
        lstVoices = voiceService.getDataByLang(uslang)
        if (isinit) userSettingService.update(INFO_USLANG, uslang)
        selectedVoiceIndex = 0.coerceAtLeast(lstVoices.indexOfFirst { it.id == usvoice })
        selectedDictReferenceIndex = 0.coerceAtLeast(lstDictsReference.indexOfFirst { it.dictid.toString() == usdictreference })
        selectedDictNoteIndex = 0.coerceAtLeast(lstDictsNote.indexOfFirst { it.dictid == usdictnote })
        selectedDictTranslationIndex = 0.coerceAtLeast(lstDictsTranslation.indexOfFirst { it.dictid == usdicttranslation })
        selectedTextbookIndex = 0.coerceAtLeast(lstTextbooks.indexOfFirst { it.id == ustextbook })
        settingsListener?.onUpdateLang()
        updateVoice(); updateDictReference(); updateDictNote(); updateDictTranslation(); updateTextbook()
        busy = false
    }

    fun updateTextbook() = viewModelScope.launch {
        if (lstTextbooks.isEmpty()) return@launch
        ustextbook = selectedTextbook.id
        INFO_USUNITFROM = getUSInfo(MUSMapping.NAME_USUNITFROM)
        INFO_USPARTFROM = getUSInfo(MUSMapping.NAME_USPARTFROM)
        INFO_USUNITTO = getUSInfo(MUSMapping.NAME_USUNITTO)
        INFO_USPARTTO = getUSInfo(MUSMapping.NAME_USPARTTO)
        toType = if (isSingleUnit) UnitPartToType.Unit else if (isSingleUnitPart) UnitPartToType.Part else UnitPartToType.To
        userSettingService.update(INFO_USTEXTBOOK, ustextbook)
        settingsListener?.onUpdateTextbook()
    }

    fun updateDictReference() = viewModelScope.launch {
        if (lstDictsReference.isEmpty()) return@launch
        usdictreference = selectedDictReference.dictid.toString()
        userSettingService.update(INFO_USDICTREFERENCE, usdictreference)
        settingsListener?.onUpdateDictReference()
    }

    fun updateDictNote() = viewModelScope.launch {
        if (lstDictsNote.isEmpty()) return@launch
        usdictnote = selectedDictNote?.dictid ?: 0
        userSettingService.update(INFO_USDICTNOTE, usdictnote)
        settingsListener?.onUpdateDictNote()
    }

    fun updateDictTranslation() = viewModelScope.launch {
        if (lstDictsTranslation.isEmpty()) return@launch
        usdicttranslation = selectedDictTranslation?.dictid ?: 0
        userSettingService.update(INFO_USDICTTRANSLATION, usdicttranslation)
        settingsListener?.onUpdateDictTranslation()
    }

    fun updateVoice() = viewModelScope.launch {
        if (lstVoices.isEmpty()) return@launch
        usvoice = selectedVoice?.id ?: 0
        val locale = Locale.getAvailableLocales().find {
            "${it.language}_${it.country}" == selectedVoice?.voicelang
        }
        if (tts.isLanguageAvailable(locale) < TextToSpeech.LANG_AVAILABLE) return@launch
        tts.language = locale
        userSettingService.update(INFO_USANDROIDVOICE, usvoice)
        withContext(Dispatchers.Main) { settingsListener?.onUpdateVoice() }
    }

    fun autoCorrectInput(text: String): String =
        autoCorrect(text, lstAutoCorrect, { it.input }, { it.extended })

    fun updateUnitFrom(v: Int) = viewModelScope.launch {
        doUpdateUnitFrom(v, false)
        if (toType == UnitPartToType.Unit)
            doUpdateSingleUnit()
        else if (toType == UnitPartToType.Part || isInvalidUnitPart)
            doUpdateUnitPartTo()
    }

    fun updatePartFrom(v: Int) = viewModelScope.launch {
        doUpdatePartFrom(v, false)
        if (toType == UnitPartToType.Part || isInvalidUnitPart)
            doUpdateUnitPartTo()
    }

    fun updateToType(v: Int) = viewModelScope.launch {
        toType = UnitPartToType.values()[v]
        if (toType == UnitPartToType.Unit)
            doUpdateSingleUnit()
        else if (toType == UnitPartToType.Part)
            doUpdateUnitPartTo()
    }

    fun toggleUnitPart(part: Int) = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            toType = UnitPartToType.Part
            // TODO async
            doUpdatePartFrom(part)
            doUpdateUnitPartTo()
        } else if (toType == UnitPartToType.Part) {
            toType = UnitPartToType.Unit
            doUpdateSingleUnit()
        }
    }

    fun previousUnitPart() = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            if (usunitfrom > 1) {
                // TODO async
                doUpdateUnitFrom(usunitfrom - 1)
                doUpdateUnitTo(usunitfrom)
            }
        } else if (uspartfrom > 1) {
            // TODO async
            doUpdatePartFrom(uspartfrom - 1)
            doUpdateUnitPartTo()
        } else if (usunitfrom > 1) {
            // TODO async
            doUpdateUnitFrom(usunitfrom - 1)
            doUpdatePartFrom(partCount)
            doUpdateUnitPartTo()
        }
    }

    fun nextUnitPart() = viewModelScope.launch {
        if (toType == UnitPartToType.Unit) {
            // TODO async
            if (usunitfrom < unitCount) {
                doUpdateUnitFrom(usunitfrom + 1)
                doUpdateUnitTo(usunitfrom)
            }
        } else if (uspartfrom < partCount) {
            // TODO async
            doUpdatePartFrom(uspartfrom + 1)
            doUpdateUnitPartTo()
        } else if (usunitfrom < unitCount) {
            // TODO async
            doUpdateUnitFrom(usunitfrom + 1)
            doUpdatePartFrom(1)
            doUpdateUnitPartTo()
        }
    }

    fun updateUnitTo(v: Int) = viewModelScope.launch {
        doUpdateUnitTo(v, false)
        if (isInvalidUnitPart)
            doUpdateUnitPartFrom()
    }

    fun updatePartTo(v: Int) = viewModelScope.launch {
        doUpdatePartTo(v, false)
        if (isInvalidUnitPart)
            doUpdateUnitPartFrom()
    }

    private suspend fun doUpdateUnitPartFrom() {
        // TODO async
        doUpdateUnitFrom(usunitto)
        doUpdatePartFrom(uspartto)
    }

    private suspend fun doUpdateUnitPartTo() {
        // TODO async
        doUpdateUnitTo(usunitfrom)
        doUpdatePartTo(uspartfrom)
    }

    private suspend fun doUpdateSingleUnit() {
        // TODO async
        doUpdateUnitTo(usunitfrom)
        doUpdatePartFrom(1)
        doUpdatePartTo(partCount)
    }

    private suspend fun doUpdateUnitFrom(v: Int, check: Boolean = true) {
        if (check && usunitfrom == v) return
        usunitfrom = v
        userSettingService.update(INFO_USUNITFROM, usunitfrom)
        settingsListener?.onUpdateUnitFrom()
    }

    private suspend fun doUpdatePartFrom(v: Int, check: Boolean = true) {
        if (check && uspartfrom == v) return
        uspartfrom = v
        userSettingService.update(INFO_USPARTFROM, uspartfrom)
        settingsListener?.onUpdatePartFrom()
    }

    private suspend fun doUpdateUnitTo(v: Int, check: Boolean = true) {
        if (check && usunitto == v) return
        usunitto = v
        userSettingService.update(INFO_USUNITTO, usunitto)
        settingsListener?.onUpdateUnitTo()
    }

    private suspend fun doUpdatePartTo(v: Int, check: Boolean = true) {
        if (check && uspartto == v) return
        uspartto = v
        userSettingService.update(INFO_USPARTTO, uspartto)
        settingsListener?.onUpdatePartTo()
    }

    suspend fun getHtml(url: String): String =
        htmlService.getHtml(url)

    suspend fun getNote(word: String): String {
        val dictNote = selectedDictNote ?: return ""
        val url = dictNote.urlString(word, lstAutoCorrect)
        val html = getHtml(url)
        Log.d("", html)
        return extractTextFrom(html, dictNote.transform, "") { text, _ -> text }
    }

    suspend fun getNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
        val dictNote = selectedDictNote ?: return
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

    suspend fun clearNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
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
