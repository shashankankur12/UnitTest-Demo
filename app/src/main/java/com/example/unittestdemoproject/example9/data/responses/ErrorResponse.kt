package com.example.unittestdemoproject.example9.data.responses

import com.example.unittestdemoproject.example9.data.responses.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResponse : BaseResponse() {
    @SerializedName("data")
    @Expose
    var data: Any? = null

    @SerializedName("errCode")
    @Expose
    var errCode: Int? = null
}