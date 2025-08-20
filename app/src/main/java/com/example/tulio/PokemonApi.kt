package com.example.tulio

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<PokemonResponse>
}
