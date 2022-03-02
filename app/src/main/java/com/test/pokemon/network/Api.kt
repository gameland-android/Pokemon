package com.test.pokemon.network

import com.test.pokemon.PokemonData
import com.test.pokemon.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path(value="id")id: Int): PokemonData

    @GET("pokemon")
    suspend fun getPokemonList(@Query(value="limit")maxNum: Int): PokemonList

}