package com.back.global.rsData

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RsData<T> (
    val resultCode: String,
    val msg: String,
    val data: T?
) {
    companion object {
        val OK = RsData("200-1", "OK", "")
    }

    @JsonIgnore
    fun getStatusCode(): Int = resultCode.split("-")[0].toInt()

    @JsonIgnore
    val isSuccess = getStatusCode() < 400

    @JsonIgnore
    val isFail = !isSuccess

    fun <U> newDataOf(data: U): RsData<U> = RsData(resultCode, msg, data)
}
