package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.GlobalConstants
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.service.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel {

    val wordFamiService = WordFamiService()

    private fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        wordFamiService.getDataByUserWord(userid, wordid)
            .applyIO()

    private fun update(o: MWordFami): Observable<Unit> =
        wordFamiService.update(o.id, o.userid, o.wordid, o.correct, o.total)
            .applyIO()

    private fun create(o: MWordFami): Observable<Int> =
        wordFamiService.create(o.userid, o.wordid, o.correct, o.total)
            .applyIO()

    private fun delete(id: Int): Observable<Unit> =
        wordFamiService.delete(id)
            .applyIO()

    fun update(wordid: Int, isCorrect: Boolean): Observable<MWordFami> {
        return getDataByUserWord(GlobalConstants.userid, wordid).flatMap { lst ->
            val d = if (isCorrect) 1 else 0
            val item = MWordFami().apply {
                userid = GlobalConstants.userid
                this.wordid = wordid
            }
            if (lst.isEmpty()) {
                item.correct = d
                item.total = 1
                create(item).map {
                    item.id = it
                    item
                }
            }
            else {
                val o = lst[0]
                item.id = o.id
                item.correct = o.correct + d
                item.total = o.total + 1
                update(item).map {
                    item
                }
            }
        }
    }
}
