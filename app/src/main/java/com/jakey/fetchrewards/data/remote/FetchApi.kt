package com.jakey.fetchrewards.data.remote

import com.jakey.fetchrewards.data.remote.responses.FetchUser
import com.jakey.fetchrewards.utils.Resource
import retrofit2.Response
import retrofit2.http.GET

interface FetchApi {

    @GET("/hiring.json")
    suspend fun getUsers(): Response<List<FetchUser>>

    companion object {
        const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    }
}