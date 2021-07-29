package com.example.unittestdemoproject.example9.data.reopsitories

import com.example.unittestdemoproject.example9.data.network.ApiInterFace
import com.example.unittestdemoproject.example9.data.network.SafeApiRequest
import com.example.unittestdemoproject.example9.data.responses.LoginUserData
import com.example.unittestdemoproject.example9.data.responses.LoginRequest

class UserRepository(private val api: ApiInterFace)  : SafeApiRequest(){
    suspend fun userLogin(userId: String, password: String): LoginUserData? =
        apiRequest { api.doLogin(LoginRequest(userId, password)) }
}