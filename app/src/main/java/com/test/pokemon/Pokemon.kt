package com.test.pokemon

import com.squareup.moshi.Json

data class Pokemon(
    val name: String,
    val id: Int
)

data class PokemonData(
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>
)

data class PokemonList(
    val results: List<NamedResource>
)

data class NamedResource(
    val name: String,
)

data class PokemonType(
    val type: NamedResource
)

data class PokemonStat(
    @field:Json(name = "base_stat")
    val baseStat: Int
)

data class PokemonSprites(
    @field:Json(name = "front_default")
    val frontDefault: String,
    @field:Json(name = "front_shiny")
    val frontShiny: String,
    @field:Json(name = "front_female")
    val frontFemale: String,
    @field:Json(name = "front_shiny_female")
    val frontShinyFemale: String,
    @field:Json(name = "back_default")
    val backDefault: String,
    @field:Json(name = "back_shiny")
    val backShiny: String,
    @field:Json(name = "back_female")
    val backFemale: String,
    @field:Json(name = "back_shiny_female")
    val backShinyFemale: String
)