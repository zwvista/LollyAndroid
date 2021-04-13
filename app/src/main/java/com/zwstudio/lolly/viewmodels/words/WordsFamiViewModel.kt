package com.zwstudio.lolly.viewmodels.words

import com.zwstudio.lolly.viewmodels.misc.Global
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.services.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable

class WordsFamiViewModel {

    val wordFamiService = WordFamiService()

    fun update(wordid: Int, isCorrect: Boolean): Observable<MWordFami> {
        return wordFamiService.getDataByWord(wordid).flatMap { lst ->
            val d = if (isCorrect) 1 else 0
            val item = MWordFami().apply {
                userid = Global.userid
                this.wordid = wordid
            }
            if (lst.isEmpty()) {
                item.correct = d
                item.total = 1
                wordFamiService.create(item).map {
                    item.id = it
                    item
                }
            }
            else {
                val o = lst[0]
                item.id = o.id
                item.correct = o.correct + d
                item.total = o.total + 1
                wordFamiService.update(item).map {
                    item
                }
            }
        }
    }
}
