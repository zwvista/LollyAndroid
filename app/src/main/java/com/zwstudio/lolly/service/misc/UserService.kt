package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MUser
import com.zwstudio.lolly.restapi.misc.RestUser

class UserService {
    suspend fun getData(username: String, password: String): List<MUser> =
        retrofitJson.create(RestUser::class.java)
            .getData("USERNAME,eq,$username", "PASSWORD,eq,$password")
            .let { it.lst!! }
}
