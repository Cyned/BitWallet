package com.example.bitwallet

import LoginModel
import BalanceModel
import ExchangeModel
import GetAddressModel
import SendModel
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.*


var URL: String = "http://176.37.12.50:8364/"


interface ApiService {

    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<LoginModel>

    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<LoginModel>

    @GET("/wallet/getBalance")
    fun getBalance(
        @Header("x-access-token") token: String
    ) : Call<BalanceModel>


    @GET("/rates/btcusd")
    fun getExchange(
        @Header("x-access-token") token: String
    ) : Call<ExchangeModel>

    @GET("/wallet/createAddress")
    fun getAddress(
        @Header("x-access-token") token: String
    ) : Call<GetAddressModel>

    @GET("/wallet/sendTransaction")
    fun send(
        @Header("x-access-token") token: String,
        @Header("address") address: String,
        @Header("amount") amount: Float,
        @Header("comment") comment: String
    ) : Call<SendModel>
}