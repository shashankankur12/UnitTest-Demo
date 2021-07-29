package com.example.unittestdemoproject.example9.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.unittestdemoproject.example9.data.network.SharedPref
import com.example.unittestdemoproject.example9.data.reopsitories.UserRepository
import com.example.unittestdemoproject.example9.data.responses.LoginUserData
import com.example.unittestdemoproject.example9.utils.AppConstant
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: UserRepository

    @Mock
    private lateinit var pref: SharedPref

    private lateinit var loginViewModel: LoginViewModel

    private val userId = "userId"
    private val password = "password"
    private val emptyUserId = ""
    private val emptyPassword = ""

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository, pref)
    }

    @Test
    fun onSignInButtonClick_onInsertEmptyUserID_fail() {
        loginViewModel.onSignInButtonClick(emptyUserId, password)
        val value = loginViewModel.getLoginData().getOrAwaitValue()

        assertThat(value.message).isEqualTo(AppConstant.ERROR_EMPTY_USERID)
    }

    @Test
    fun onSignInButtonClick_onInsertEmptyPassword_fail() {
        loginViewModel.onSignInButtonClick(userId, emptyPassword)
        val value = loginViewModel.getLoginData().getOrAwaitValue()

        assertThat(value.message).isEqualTo(AppConstant.ERROR_EMPTY_PASSWORD)
    }

    @Test
    fun onSignInButtonClick_onInsertValidDetails_pass() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.userLogin(userId, password))
                .thenReturn(getLoginUserData())
            loginViewModel.hitLoginApi(userId, password)
            val value = loginViewModel.getLoginData().getOrAwaitValue()

            assertThat(value.data?.token).isEqualTo(AppConstant.TOKEN)
        }
    }

    @Test
    fun onSignInButtonClick_onInsertInValidDetails_fail() {
        mainCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.userLogin(userId, password)).thenReturn(null)
            loginViewModel.hitLoginApi(userId, password)
            val value = loginViewModel.getLoginData().getOrAwaitValue()

            assertThat(value.message).isEqualTo(AppConstant.ERROR_NO_USER_FOUND)
        }

    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    private fun getLoginUserData(): LoginUserData {
        val loginUserData = LoginUserData()
        loginUserData.token = AppConstant.TOKEN
        return loginUserData
    }

}

