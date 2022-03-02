package com.test.pokemon.model

import com.test.pokemon.Pokemon
import com.test.pokemon.PokemonData
import io.uniflow.core.flow.data.UIState

object InitialState: UIState()

object LoadingListState: UIState()

data class LoadingPokemonState(val list: List<Pokemon>): UIState()

data class ShowingListState(val list: List<Pokemon>): UIState()

data class ShowingPokemonState(val pokemon: PokemonData): UIState()

data class ErrorState(val message: String?): UIState()

data class SearchResultState(val list: List<Pokemon>): UIState()

data class NoResultState(val message: String?): UIState()