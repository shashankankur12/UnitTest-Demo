package com.example.unittestdemoproject.example9.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.unittestdemoproject.R
import com.example.unittestdemoproject.databinding.ActivityLoginBinding
import com.example.unittestdemoproject.example9.utils.Resource
import com.example.unittestdemoproject.example9.utils.Status
import com.example.unittestdemoproject.example9.utils.snackbar
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: LoginViewModelFactory by instance()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@LoginActivity, R.layout.activity_login)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)
        setDataObserver()

        buttonLogin.setOnClickListener {
            val userId = editTextEmployeeId.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            viewModel.onSignInButtonClick(userId, password)

        }
    }

    private fun setDataObserver() {
        viewModel.getLoginData().observe(this, Observer { data ->
            when (data?.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    data.let {
                        mainRootLayout.snackbar("Success")
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    data.message?.let { mainRootLayout.snackbar(it) }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}