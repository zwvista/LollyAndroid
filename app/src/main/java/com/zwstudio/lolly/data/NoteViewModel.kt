package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.DictNote
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean
import java.util.concurrent.TimeUnit

@EBean
class NoteViewModel : BaseViewModel2() {

    lateinit var compositeDisposable: CompositeDisposable
    val dictNote: DictNote?
        get() = vmSettings.selectedDictNote

    fun getNote(word: String): Observable<String> {
        val dictNote = dictNote ?: return Observable.empty()
        val url = dictNote.urlString(word, vmSettings.lstAutoCorrect)
        return getHtml(url).map {
            Log.d("", it)
            extractTextFrom(it, dictNote.transform!!, "") { text, _ -> text }
        }
    }

    fun getNotes(wordCount: Int, isNoteEmpty: (Int) -> Boolean, getOne: (Int) -> Unit, allComplete: () -> Unit) {
        val dictNote = dictNote ?: return
        var i = 0
        var subscription: Disposable? = null
        subscription = Observable.interval(dictNote.wait!!.toLong(), TimeUnit.MILLISECONDS, Schedulers.io()).subscribe {
            while (i < wordCount && !isNoteEmpty(i))
                i++;
            if (i > wordCount) {
                allComplete()
                subscription?.dispose()
            } else {
                if (i < wordCount)
                    getOne(i)
                i++;
            }
        }
        compositeDisposable.add(subscription)
    }
}