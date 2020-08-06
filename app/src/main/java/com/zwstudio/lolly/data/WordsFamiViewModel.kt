package com.zwstudio.lolly.data

import com.zwstudio.lolly.data.GlobalConstants.userid
import com.zwstudio.lolly.domain.MWordFami
import com.zwstudio.lolly.service.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel : BaseViewModel1() {

    @Bean
    lateinit var wordFamiService: WordFamiService

    private fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        wordFamiService.getDataByUserWord(userid, wordid)
            .applyIO()

    private fun update(id: Int, userid: Int, wordid: Int, level: Int, correct: Int, total: Int): Observable<Int> =
        wordFamiService.update(id, userid, wordid, level, correct, total)
            .applyIO()

    private fun create(userid: Int, wordid: Int, level: Int, correct: Int, total: Int): Observable<Int> =
        wordFamiService.create(userid, wordid, level, correct, total)
            .applyIO()

    private fun delete(id: Int): Observable<Int> =
        wordFamiService.delete(id)
            .applyIO()

    fun update(wordid: Int, level: Int): Observable<Int> {
        return getDataByUserWord(GlobalConstants.userid, wordid).concatMap { lst ->
            if (lst.isEmpty()) {
                if (level == 0)
                    Observable.empty<Int>()
                else
                    create(userid, wordid, level, 0, 0)
            } else {
                val o = lst[0]
                if (level == 0 && o.correct == 0 && o.total == 0)
                    delete(o.id)
                else
                    update(o.id, userid, wordid, level, o.correct, o.total)
            }
        }
    }

    fun update(wordid: Int, isCorrect: Boolean): Observable<Int> {
        return getDataByUserWord(GlobalConstants.userid, wordid).concatMap { lst ->
            val d = if (isCorrect) 1 else 0
            if (lst.isEmpty())
                create(userid, wordid, 0, d, 1)
            else {
                val o = lst[0]
                update(o.id, userid, o.wordid, o.level, o.correct + d, o.total + 1)
            }
        }
    }

    fun clearAccuracy(wordid: Int): Observable<Int> {
        return getDataByUserWord(GlobalConstants.userid, wordid).concatMap { lst ->
            if (lst.isEmpty())
                Observable.empty()
            else {
                val o = lst[0]
                if (o.level == 0)
                    delete(wordid)
                else
                    update(o.id, userid, o.wordid, o.level, 0, 0)
            }
        }
    }
}
