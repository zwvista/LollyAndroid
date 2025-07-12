package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser
import io.reactivex.rxjava3.core.Single

class UserService {
    private val api = retrofitJson.create(RestUser::class.java)

    fun getData(username: String, password: String): Single<List<MUser>> =
        api.getData("USERNAME,eq,$username", "PASSWORD,eq,$password")
            .map { it.lst }
}
