package com.jakey.fetchrewards.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakey.fetchrewards.data.remote.responses.FetchUser
import com.jakey.fetchrewards.domain.FetchRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"
class FetchViewModel: ViewModel() {

    private val repository = FetchRepository()

    private val _fetchUsers = MutableLiveData<List<FetchUser>>(emptyList())
    val fetchUsers: LiveData<List<FetchUser>> = _fetchUsers

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading: LiveData<Boolean> = _isLoading

    fun getUsers() = viewModelScope.launch {
        val response = try {
            _isLoading.postValue(true)
            repository.getUsers()
        } catch (e: IOException) {
            Log.e(TAG, "IOException. Check your internet connection.")
            return@launch
        } catch (e: HttpException) {
            Log.e(TAG, "HttpException, unexpected response.")
            return@launch
        }
        if (response.isSuccessful && response.body() != null) {
            _isLoading.postValue(false)
            _fetchUsers.postValue(response.body())
        } else {
            Log.e(TAG, "Response not successful.")
        }
    }
}