package com.test.pokemon.gui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.commit
import com.test.pokemon.*
import com.test.pokemon.databinding.ActivityMainBinding
import com.test.pokemon.gui.fragment.PokemonFragment
import com.test.pokemon.model.*
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dataFlow by viewModel<MainDataFlow>()
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        onEvents(dataFlow) { event ->
            when (event) {
                is LoadPokemonEvent -> showPokemonFragment()
                is UpdatePokemonEvent -> removeListeners()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val menuItem = menu.findItem(R.id.search)
        (menuItem.actionView as SearchView).apply {
            queryHint = "search pokemon"
            background = ResourcesCompat.getDrawable(resources, R.drawable.search_background, null)
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?) = true
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null)
                        dataFlow.searchPokemon(query)
                    return true
                }
            })
        }
        menuItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?) = true
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                dataFlow.restoreList()
                return true
            }
        })

        onStates(dataFlow) { state ->
            when (state) {
                is ShowingPokemonState ->
                    showPokemonToolbar(state.pokemon.name)
                is ErrorState, LoadingListState, is LoadingPokemonState ->
                    showMainToolbar(enableSearch = false, expandSearchView = false)
                is SearchResultState, is NoResultState ->
                    showMainToolbar(enableSearch = true, expandSearchView = true)
                is ShowingListState ->
                    showMainToolbar(enableSearch = true, expandSearchView = false)
            }
        }
        return true
    }

    private fun showMainToolbar(enableSearch: Boolean, expandSearchView: Boolean) {
        binding.mainToolbar.visibility = View.VISIBLE
        binding.pokemonToolbar.visibility = View.GONE
        menu.findItem(R.id.search).apply {
            isEnabled = enableSearch
            if (expandSearchView)
                expandActionView()
        }
    }

    private fun showPokemonToolbar(name: String) {
        binding.pokemonToolbar.visibility = View.VISIBLE
        binding.mainToolbar.visibility = View.GONE
        binding.pokemonName.text = name.uppercase()
        binding.previous.setOnClickListener {
            dataFlow.showPreviousElement()
        }
        binding.next.setOnClickListener {
            dataFlow.showNextElement()
        }
    }

    // disable listeners while pokemon data are loading
    private fun removeListeners() {
        binding.previous.setOnClickListener(null)
        binding.next.setOnClickListener(null)
    }

    private fun showPokemonFragment() {
        menu.findItem(R.id.search).collapseActionView()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace(binding.fragmentContainerView.id, PokemonFragment())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dataFlow.showList()
    }

}