package com.zwstudio.lolly.data

import com.zwstudio.lolly.data.GlobalConstants.userid
import com.zwstudio.lolly.domain.MWordFami
import com.zwstudio.lolly.service.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel {

    @Bean
    lateinit var wordFamiService: WordFamiService

    private fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        wordFamiService.getDataByUserWord(userid, wordid)
            .applyIO()

    private fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int): Observable<Unit> =
        wordFamiService.update(id, userid, wordid, correct, total)
            .applyIO()

    private fun create(userid: Int, wordid: Int, correct: Int, total: Int): Observable<Unit> =
        wordFamiService.create(userid, wordid, correct, total)
            .applyIO()

    private fun delete(id: Int): Observable<Unit> =
        wordFamiService.delete(id)
            .applyIO()

    fun update(wordid: Int, isCorrect: Boolean): Observable<Unit> {
        return getDataByUserWord(GlobalConstants.userid, wordid).flatMap { lst ->
            val d = if (isCorrect) 1 else 0
            if (lst.isEmpty())
                create(userid, wordid, d, 1)
            else {
                val o = lst[0]
                update(o.id, userid, o.wordid,  o.correct + d, o.total + 1)
            }
        }
    }
}
