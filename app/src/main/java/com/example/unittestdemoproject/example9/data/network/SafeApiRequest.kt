package com.example.unittestdemoproject.example9.data.network

import com.example.unittestdemoproject.example9.data.responses.ErrorResponse
import com.example.unittestdemoproject.example9.utils.ApiExceptions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T? {
        val response = call.invoke()
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        } else {
            val errorBody = response.errorBody()
            var errorCode: ErrorResponse

            errorBody.let {
                val errorBodyString = String(it?.bytes()!!)
                errorCode = try {
                    Gson().fromJson(errorBodyString, ErrorResponse::class.java)
                } catch (e: JsonSyntaxException) {
                    errorBodyString
                } as ErrorResponse
                throw ApiExceptions(errorCode.message)
            }
        }
    }
}