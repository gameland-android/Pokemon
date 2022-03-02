package com.test.pokemon.gui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
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

class ListFragment : Fragment(R.layout.list_fragment), View.OnTouchListener {

    private lateinit var binding: ListFragmentBinding
    private val dataFlow by sharedViewModel<MainDataFlow>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ListFragmentBinding.bind(view)

        onStates(dataFlow) { state ->
            when (state) {
                is InitialState -> splashScreen()
                is ErrorState -> showError(state.message, true)
                is ShowingListState -> showList(state.list, false, null)
                is SearchResultState -> showList(state.list, false, null)
                is LoadingPokemonState -> showList(state.list, true, null)
                is NoResultState -> showList(emptyList(), false, state.message)
                is LoadingListState -> reload()
            }
        }
        onEvents(dataFlow) { event ->
            when (event) {
                is ErrorEvent -> showError(event.message, false)
            }
        }
    }

    private fun splashScreen() {
        binding.recyclerView.visibility = View.GONE
    }

    private fun showError(message: String?, swipeToReload: Boolean) {
        binding.loading.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorIcon.visibility = if (swipeToReload) View.VISIBLE else View.GONE
        binding.errorMessage.apply {
            visibility = View.VISIBLE
            if (message != null)
                text = message
        }
        if (swipeToReload)
            binding.mainLayout.setOnTouchListener(this)
    }

    private fun showList(pokemonList: List<Pokemon>, loading: Boolean, message: String?) {
        if (binding.recyclerView.adapter == null) {
            binding.recyclerView.adapter = PokemonListAdapter(pokemonList, dataFlow)
            val span = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 4
            binding.recyclerView.layoutManager = GridLayoutManager(context, span)
            binding.recyclerView.visibility = View.VISIBLE
        } else if (!loading)
            (binding.recyclerView.adapter as PokemonListAdapter).apply {
                this.pokemonList = pokemonList
                notifyDataSetChanged()
            }
        (binding.recyclerView.adapter as PokemonListAdapter).listenerEnabled = !loading
        binding.loading.visibility = if (loading) View.VISIBLE else View.GONE
        if (message != null) {
            binding.errorMessage.text = message
            binding.errorMessage.visibility = View.VISIBLE
        } else
            binding.errorMessage.visibility = View.GONE
        binding.errorIcon.visibility = View.GONE
        binding.mainLayout.setOnTouchListener(null)
    }

    private fun reload() {
        binding.loading.visibility = View.VISIBLE
        binding.errorMessage.visibility = View.GONE
        binding.errorIcon.visibility = View.GONE
        binding.mainLayout.setOnTouchListener(null)
    }

    var y: Float? = null
    // listen for user swiping down
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN ->  y = event.rawY
            MotionEvent.ACTION_MOVE -> {
                if (event.rawY > y!!)
                    dataFlow.reloadList()
                y = event.rawY
            }
        }
        return true
    }

}