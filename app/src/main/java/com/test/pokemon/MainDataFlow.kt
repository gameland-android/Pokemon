package com.test.pokemon

import com.test.pokemon.model.*
import io.uniflow.android.AndroidDataFlow
import io.uniflow.core.flow.actionOn
import java.io.IOException
import kotlin.properties.Delegates

class MainDataFlow(private val repository: Repository): AndroidDataFlow(defaultState = InitialState) {

    private lateinit var list: List<Pokemon>
    private var subList: List<Pokemon> = emptyList()
    private var currentIndex by Delegates.notNull<Int>()

    init {
        action {
            getList()
        }
    }

    fun reloadList() = actionOn<ErrorState> {
        setState(LoadingListState)
        getList()
    }

    fun showList() = actionOn<ShowingPokemonState> {
        if (subList.isEmpty())
            setState(ShowingListState(list))
        else
            setState(SearchResultState(subList))
    }

    fun showPokemonData(id: Int, currentIndex: Int) = action {
        this.currentIndex = currentIndex
        setState(LoadingPokemonState(subList.ifEmpty { list }))
        getPokemonData(id)
    }

    fun searchPokemon(query: String) = action {
        subList = list.filter { it.name.contains(query, ignoreCase = true) }
        if (subList.isNotEmpty())
            setState(SearchResultState(subList))
        else
            setState(NoResultState("No matching results"))
    }

    fun restoreList() = action { currentState ->
        if (currentState !is ShowingPokemonState) {
            subList = emptyList()
            setState(ShowingListState(list))
        }
    }

    fun showPreviousElement() = actionOn<ShowingPokemonState> {
        sendEvent(UpdatePokemonEvent)
        val currentList = subList.ifEmpty { list }
        if (currentList.size > 1) {
            if (--currentIndex < 0)
                currentIndex = currentList.size - 1
            getPokemonData(currentList[currentIndex].id)
        }
    }

    fun showNextElement() = actionOn<ShowingPokemonState> {
        sendEvent(UpdatePokemonEvent)
        val currentList = subList.ifEmpty { list }
        if (currentList.size > 1) {
            if (++currentIndex == currentList.size)
                currentIndex = 0
            getPokemonData(currentList[currentIndex].id)
        }
    }

    private fun getList() = action(
        onAction = {
            list = repository.getPokemonList()
            setState(ShowingListState(list))
        },
        onError = { error, _ ->
            when (error) {
                is IOException -> setState(ErrorState("Please check your internet connection!\nSwipe down to reload"))
                else -> setState(ErrorState("An error has occurred!\nSwipe down to reload"))
            }
        }
    )

    private fun getPokemonData(id: Int) = action(
        onAction = { currentState ->
            val pokemon = repository.getPokemon(id)
            if (currentState !is ShowingPokemonState)
                sendEvent(LoadPokemonEvent)
            setState(ShowingPokemonState(pokemon))
        },
        onError = { _, currentState ->
            if (currentState is ShowingPokemonState)
                return@action
            if (subList.isEmpty())
                setState(ShowingListState(list))
            else
                setState(SearchResultState(subList))
            sendEvent(ErrorEvent("An error has occurred! Please retry"))
        }
    )

}