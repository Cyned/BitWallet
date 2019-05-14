package com.example.bitwallet

import LoginModel
import retrofit2.Call
import retrofit2.http.*


var URL: String = "http://176.37.12.50:8364/"


interface ApiService {

    @POST("auth/login")
    @FormUrlEncoded
    fun NewUser(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<LoginModel>
}