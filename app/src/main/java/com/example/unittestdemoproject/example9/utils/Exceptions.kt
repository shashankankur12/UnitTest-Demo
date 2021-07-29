package com.example.unittestdemoproject.example9.utils

import java.io.IOException

class ApiExceptions(message:String) : IOException(message)
class NoInternetException(message: String) : IOException(message)