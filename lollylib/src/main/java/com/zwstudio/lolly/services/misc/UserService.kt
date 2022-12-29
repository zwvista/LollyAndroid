package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson

class UserService {
    fun getData(username: String, password: String): Single<List<MUser>> =
        retrofitJson.create(RestUser::class.java)
            .getData("USERNAME,eq,$username", "PASSWORD,eq,$password")
            .map { it.lst!! }
}
