package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser

class UserService {
    private val api = retrofitJson.create(RestUser::class.java)

    suspend fun getData(username: String, password: String): List<MUser> =
        api.getData("USERNAME,eq,$username", "PASSWORD,eq,$password").lst
}
