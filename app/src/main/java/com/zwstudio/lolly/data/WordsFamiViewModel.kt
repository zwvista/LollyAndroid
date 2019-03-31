package com.zwstudio.lolly.data

import com.zwstudio.lolly.data.GlobalConstants.userid
import com.zwstudio.lolly.domain.MWordFami
import com.zwstudio.lolly.service.WordFamiService
import io.reactivex.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel : BaseViewModel1() {

    @Bean
    lateinit var wordFamiService: WordFamiService;

    private fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        wordFamiService.getDataByUserWord(userid, wordid)
            .applyIO()

    private fun update(id: Int, userid: Int, wordid: Int, level: Int): Observable<Int> =
        wordFamiService.update(id, userid, wordid, level)
            .applyIO()

    private fun create(userid: Int, wordid: Int, level: Int): Observable<Int> =
        wordFamiService.create(userid, wordid, level)
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
                    create(userid, wordid, level)
            } else {
                val id = lst[0].id
                if (level == 0)
                    delete(id)
                else
                    update(id, userid, wordid, level)
            }
        }
    }

}
