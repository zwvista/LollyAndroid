package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser
import io.reactivex.rxjava3.core.Observable

class UserService {
    fun getData(username: String, password: String): Observable<List<MUser>> =
        retrofitJson.create(RestUser::class.java)
            .getData("USERNAME,eq,$username", "PASSWORD,eq,$password")
            .map { it.lst!! }
}
