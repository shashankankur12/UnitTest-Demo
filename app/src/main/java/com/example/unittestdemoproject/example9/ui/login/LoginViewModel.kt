package com.example.unittestdemoproject.example9.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unittestdemoproject.example9.data.network.SharedPref
import com.example.unittestdemoproject.example9.data.reopsitories.UserRepository
import com.example.unittestdemoproject.example9.data.responses.LoginUserData
import com.example.unittestdemoproject.example9.utils.ApiExceptions
import com.example.unittestdemoproject.example9.utils.AppConstant
import com.example.unittestdemoproject.example9.utils.NoInternetException
import com.example.unittestdemoproject.example9.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository, private val pref: SharedPref) :
    ViewModel() {

    private val loginUserData = MutableLiveData<Resource<LoginUserData>>()

    fun getLoginData(): LiveData<Resource<LoginUserData>> {
        return loginUserData
    }

    fun onSignInButtonClick(userId: String, password: String) {
        loginUserData.postValue(Resource.loading())
        when {
            userId.isNullOrEmpty() -> {
                loginUserData.postValue(Resource.error(AppConstant.ERROR_EMPTY_USERID))
            }
            password.isNullOrEmpty() -> {
                loginUserData.postValue(Resource.error(AppConstant.ERROR_EMPTY_PASSWORD))
            }
            else -> {
                hitLoginApi(userId, password)
            }
        }
    }

    fun hitLoginApi(userId: String, password: String) {
        viewModelScope.launch {
            try {
                val loginResponse = repository.userLogin(userId, password)
                loginResponse?.also {
                    it.token?.let { token ->
                        pref.token = token
                        loginUserData.postValue(Resource.success(it))
                    }
                } ?: run {
                    loginUserData.postValue(Resource.error(AppConstant.ERROR_NO_USER_FOUND))
                }

            } catch (e: ApiExceptions) {
                e.message?.let {
                    loginUserData.postValue(Resource.error(it))
                }

            } catch (e: NoInternetException) {
                e.message?.let {
                    loginUserData.postValue(Resource.error(it))
                }
            }
        }
    }
}