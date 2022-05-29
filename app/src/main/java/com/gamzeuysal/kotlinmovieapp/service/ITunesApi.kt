package com.gamzeuysal.kotlinmovieapp.service

import android.text.Editable
import com.gamzeuysal.kotlinmovieapp.model.Root
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    fun getItunesSearch(@Query("term") term: String?=null,
                        @Query("offset") offset:Int,
                        @Query("limit") limit:Int): Observable<Root>//Observable veriler geldiginde bunları alan ve yayın yapan objedir

}