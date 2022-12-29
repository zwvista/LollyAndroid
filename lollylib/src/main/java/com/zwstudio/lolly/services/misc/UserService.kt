package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser
import com.zwstudio.lolly.retrofitJson

class UserService {
    suspend fun getData(username: String, password: String): List<MUser> =
        retrofitJson.create(RestUser::class.java)
            .getData("USERNAME,eq,$username", "PASSWORD,eq,$password")
            .let { it.lst!! }
}
