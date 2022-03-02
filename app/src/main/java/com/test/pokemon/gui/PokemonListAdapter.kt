package com.test.pokemon.gui

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.pokemon.MainDataFlow
import com.test.pokemon.Pokemon
import com.test.pokemon.R
import com.test.pokemon.databinding.PokemonCardBinding

class PokemonListAdapter(var pokemonList: List<Pokemon>, val dataFlow: MainDataFlow):
    RecyclerView.Adapter<PokemonListAdapter.ViewHolder>() {

    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
    val fileSuffix = ".png"
    var listenerEnabled = true

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_card, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = PokemonCardBinding.bind(holder.itemView)
        binding.pokemonName.text = pokemonList[position].name
        val url = imageUrl + pokemonList[position].id + fileSuffix
        Glide.with(holder.itemView.context).load(url).into(binding.thumb)
    }

    override fun getItemCount() = pokemonList.size

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener, View.OnTouchListener {

        init {
            view.setOnTouchListener(this)
            view.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (listenerEnabled)
                dataFlow.showPokemonData(pokemonList[bindingAdapterPosition].id, bindingAdapterPosition)
        }

        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            if (!listenerEnabled)
                return false
            // simulating button press on CardView
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN ->
                    if (view is CardView) {
                        (view.layoutParams as GridLayoutManager.LayoutParams).apply {
                            topMargin += 10
                            bottomMargin -= 10
                            leftMargin += 10
                            rightMargin -= 10
                            view.layoutParams = this
                        }
                        view.elevation = 0F
                        view.getChildAt(0).setBackgroundColor(view.context.resources.getColor(
                            R.color.light_orange,
                            null))
                    }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                    if (view is CardView) {
                        (view.layoutParams as GridLayoutManager.LayoutParams).apply {
                            topMargin -= 10
                            bottomMargin += 10
                            leftMargin -= 10
                            rightMargin += 10
                            view.layoutParams = this
                        }
                        view.elevation = view.context.resources.getDimension(R.dimen.elevation)
                        view.getChildAt(0).setBackgroundColor(view.context.resources.getColor(
                            R.color.orange,
                            null))
                        if (motionEvent.action == MotionEvent.ACTION_UP)
                            view.performClick()
                    }
            }
            return true
        }

    }

}