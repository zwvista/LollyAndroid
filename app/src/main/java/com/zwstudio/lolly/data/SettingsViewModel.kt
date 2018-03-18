package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.Dictionary
import com.zwstudio.lolly.domain.Language
import com.zwstudio.lolly.domain.Textbook
import com.zwstudio.lolly.domain.UserSetting
import com.zwstudio.lolly.restapi.RestDictionary
import com.zwstudio.lolly.restapi.RestLanguage
import com.zwstudio.lolly.restapi.RestTextbook
import com.zwstudio.lolly.restapi.RestUserSetting
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean(scope = EBean.Scope.Singleton)
class SettingsViewModel : BaseViewModel1() {

    val userid = 1

    var lstUserSettings = listOf<UserSetting>()
    private var selectedUSUserIndex = 0
    val selectedUSUser: UserSetting
        get() = lstUserSettings[selectedUSUserIndex]
    var uslangid: Int
        get() = selectedUSUser.value1?.toInt()!!
        set(value) {
            selectedUSUser.value1 = value.toString()
        }
    private var selectedUSLangIndex = 0
    val selectedUSLang: UserSetting
        get() = lstUserSettings[selectedUSLangIndex]
    var ustextbookid: Int
        get() = selectedUSLang.value1?.toInt()!!
        set(value) {
            selectedUSLang.value1 = value.toString()
        }
    var usdictid: Int
        get() = selectedUSLang.value2?.toInt()!!
        set(value) {
            selectedUSLang.value2 = value.toString()
        }
    private var selectedUSTextbookIndex = 0
    val selectedUSTextbook: UserSetting
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
    val issingleunitpart: Boolean
        get() = usunitpartfrom == usunitpartto

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

    var lstDictionaries = listOf<Dictionary>()
    var selectedDictIndex: Int = 0
        set(value) {
            field = value
            setSelectedDictIndex()
        }
    val selectedDict: Dictionary
        get() = lstDictionaries[selectedDictIndex]

    var word = ""
    var lstUnits = listOf<String>()
    var lstParts = listOf<String>()

    fun getData(onNext: () -> Unit) {
        retrofit.create(RestLanguage::class.java)
            .getData()
            .flatMap {
                lstLanguages = it.lst!!
                retrofit.create(RestUserSetting::class.java)
                    .getDataByUser("USERID,eq,$userid")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lstUserSettings = it.lst!!
                selectedUSUserIndex = lstUserSettings.indexOfFirst { it.kind == 1 }
                setSelectedLangIndex(lstLanguages.indexOfFirst { it.id == uslangid }, onNext)
            }
    }

    fun setSelectedLangIndex(langIndex: Int, onNext: () -> Unit) {
        selectedLangIndex = langIndex
        uslangid = selectedLang.id
        selectedUSLangIndex = lstUserSettings.indexOfFirst { it.kind == 2 && it.entityid == uslangid }
        retrofit.create(RestDictionary::class.java)
            .getDataByLang("LANGIDFROM,eq,$uslangid")
            .flatMap {
                lstDictionaries = it.lst!!
                selectedDictIndex = lstDictionaries.indexOfFirst { it.id == usdictid }
                retrofit.create(RestTextbook::class.java)
                    .getDataByLang("LANGID,eq,$uslangid")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lstTextbooks = it.lst!!
                selectedTextbookIndex = lstTextbooks.indexOfFirst { it.id == ustextbookid }
            }
    }

    fun setSelectedTextbookIndex() {
        ustextbookid = selectedTextbook.id
        selectedUSTextbookIndex = lstUserSettings.indexOfFirst { it.kind == 3 && it.entityid == ustextbookid }
        lstUnits = (1..selectedTextbook.units).map { it.toString() }
        lstParts = selectedTextbook.parts?.split(' ')!!
    }

    fun setSelectedDictIndex() {
        usdictid = selectedDict.id
    }

    fun updateLang(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updateLang(selectedUSUser.id, uslangid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updateTextbook(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updateTextbook(selectedUSLang.id, ustextbookid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updateDict(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updateDict(selectedUSLang.id, usdictid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updateUnitFrom(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updateUnitFrom(selectedUSTextbook.id, usunitfrom)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updatePartFrom(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updatePartFrom(selectedUSTextbook.id, uspartfrom)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updateUnitTo(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updateUnitTo(selectedUSTextbook.id, usunitto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updatePartTo(onNext: () -> Unit) {
        retrofit.create(RestUserSetting::class.java)
            .updatePartTo(selectedUSTextbook.id, uspartto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }
}
