package com.jakey.fetchrewards.domain

import com.jakey.fetchrewards.data.remote.RetrofitInstance

class FetchRepository {

    private val api = RetrofitInstance.api

    suspend fun getUsers() = api.getUsers()

}
