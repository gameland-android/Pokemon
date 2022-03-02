package com.test.pokemon

import com.test.pokemon.network.Api

class Repository(private val api: Api) {

    private val totalCount = 898

    suspend fun getPokemonList(): List<Pokemon> {
        val pokemonList: MutableList<Pokemon> = mutableListOf()
        for ((index, pokemon) in api.getPokemonList(totalCount).results.withIndex())
            pokemonList.add(Pokemon(pokemon.name, index + 1))
        return pokemonList
    }

    suspend fun getPokemon(id: Int): PokemonData = api.getPokemon(id)

}