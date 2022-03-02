package com.test.pokemon.model

import io.uniflow.core.flow.data.UIEvent

data class ErrorEvent(val message: String?): UIEvent()

object LoadPokemonEvent: UIEvent()

object UpdatePokemonEvent: UIEvent()