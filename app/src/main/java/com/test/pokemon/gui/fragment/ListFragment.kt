package com.test.pokemon.gui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.test.pokemon.*
import com.test.pokemon.databinding.ListFragmentBinding
import com.test.pokemon.gui.PokemonListAdapter
import com.test.pokemon.model.*
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ListFragment : Fragment(R.layout.list_fragment) {

    private lateinit var binding: ListFragmentBinding
    private val dataFlow by sharedViewModel<MainDataFlow>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ListFragmentBinding.bind(view)

        onStates(dataFlow) { state ->
            when (state) {
                is InitialState -> splashScreen()
                is ErrorState -> swipeToReload(state.message)
                is ShowingListState -> showList(state.list)
                is SearchResultState -> showList(state.list)
                is LoadingPokemonState -> showLoadingList(state.list)
                is NoResultState -> showEmptyList(state.message)
                is LoadingListState -> reload()
            }
        }
        onEvents(dataFlow) { event ->
            when (event) {
                is ErrorEvent -> showError(event.message)
            }
        }
    }

    private fun splashScreen() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showError(message: String?) {
        binding.loading.visibility = View.GONE
        if (message != null) {
            binding.errorMessage.text = message
            binding.errorMessage.visibility = View.VISIBLE
        }
    }

    private fun swipeToReload(message: String?) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.errorIcon.visibility = View.VISIBLE
        if (message != null) {
            binding.errorMessage.text = message
            binding.errorMessage.visibility = View.VISIBLE
        }
        if (binding.swipeRefreshLayout.isRefreshing)
            binding.swipeRefreshLayout.isRefreshing = false
        else
            binding.swipeRefreshLayout.setOnRefreshListener { dataFlow.reloadList() }
    }

    private fun showEmptyList(message: String?) {
        if (binding.recyclerView.adapter != null)
            updateList(emptyList())
        if (message != null) {
            binding.errorMessage.text = message
            binding.errorMessage.visibility = View.VISIBLE
        }
        binding.loading.visibility = View.GONE
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun showLoadingList(pokemonList: List<Pokemon>) {
        if (binding.recyclerView.adapter == null)
            initializeList(pokemonList)
        (binding.recyclerView.adapter as PokemonListAdapter).listenerEnabled = false
        binding.loading.visibility = View.VISIBLE
        binding.errorMessage.visibility = View.GONE
        binding.errorIcon.visibility = View.GONE
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun showList(pokemonList: List<Pokemon>) {
        if (binding.recyclerView.adapter == null) {
            initializeList(pokemonList)
            binding.recyclerView.visibility = View.VISIBLE
        } else
            updateList(pokemonList)
        (binding.recyclerView.adapter as PokemonListAdapter).listenerEnabled = true
        binding.loading.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.errorIcon.visibility = View.GONE
        binding.swipeRefreshLayout.isRefreshing = false
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun reload() {
        binding.loading.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.errorIcon.visibility = View.GONE
    }

    private fun initializeList(pokemonList: List<Pokemon>) {
        binding.recyclerView.adapter = PokemonListAdapter(pokemonList) { pokemon: Pokemon, position: Int ->
            dataFlow.showPokemonData(pokemon.id, position)
        }
        val span = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
        binding.recyclerView.layoutManager = GridLayoutManager(context, span)
    }

    private fun updateList(pokemonList: List<Pokemon>) {
        (binding.recyclerView.adapter as PokemonListAdapter).apply {
            this.pokemonList = pokemonList
            notifyDataSetChanged()
        }
    }

}