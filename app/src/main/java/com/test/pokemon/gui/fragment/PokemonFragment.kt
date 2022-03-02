package com.test.pokemon.gui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.test.pokemon.*
import com.test.pokemon.databinding.PokemonFragmentBinding
import com.test.pokemon.model.ShowingPokemonState
import com.test.pokemon.utility.Converter
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.math.roundToInt

class PokemonFragment: Fragment(R.layout.pokemon_fragment) {

    private lateinit var binding: PokemonFragmentBinding
    private val dataFlow by sharedViewModel<MainDataFlow>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PokemonFragmentBinding.bind(view)

        onStates(dataFlow) { state ->
            when (state) {
                is ShowingPokemonState -> loadData(state.pokemon)
            }
        }
    }

    private fun loadData(pokemonData: PokemonData) {
        // load types
        var typeName = pokemonData.types[0].type.name
        binding.typeImage1.setImageResource(resources.getIdentifier(typeName,
            "drawable", "com.test.pokemon"))
        binding.typeText1.text = typeName.uppercase(Locale.getDefault())
        if (pokemonData.types.size > 1) {
            typeName = pokemonData.types[1].type.name
            binding.typeImage2.setImageResource(resources.getIdentifier(typeName,
                "drawable", "com.test.pokemon"))
            binding.typeText2.text = typeName.uppercase(Locale.getDefault())
            binding.type2.visibility = View.VISIBLE
        } else
            binding.type2.visibility = View.GONE
        // load height and weight
        binding.height.text = "Height: ${Converter.getFormattedHeight(pokemonData.height)}"
        binding.weight.text = "Weight: ${Converter.getFormattedWeight(pokemonData.weight)}"
        //load statistics
        binding.hp.text = pokemonData.stats[0].baseStat.toString()
        binding.attack.text = pokemonData.stats[1].baseStat.toString()
        binding.defense.text = pokemonData.stats[2].baseStat.toString()
        binding.specialAttack.text = pokemonData.stats[3].baseStat.toString()
        binding.specialDefense.text = pokemonData.stats[4].baseStat.toString()
        binding.speed.text = pokemonData.stats[5].baseStat.toString()
        // load sprites
        val glideRequestManager = Glide.with(this)
        val loadImage = { image: String?, imageView: ImageView ->
            if (image != null) {
                // restore original size
                imageView.layoutParams.apply {
                    width = (100 * resources.displayMetrics.density).roundToInt()
                    imageView.layoutParams = this
                }
                imageView.visibility = View.INVISIBLE
                glideRequestManager.load(image).into(imageView)
            } else
                imageView.visibility = View.GONE
        }
        loadImage(pokemonData.sprites.frontDefault, binding.frontDefault)
        loadImage(pokemonData.sprites.frontShiny, binding.frontShiny)
        loadImage(pokemonData.sprites.frontFemale, binding.frontFemale)
        loadImage(pokemonData.sprites.frontShinyFemale, binding.frontShinyFemale)
        loadImage(pokemonData.sprites.backDefault, binding.backDefault)
        loadImage(pokemonData.sprites.backShiny, binding.backShiny)
        loadImage(pokemonData.sprites.backFemale, binding.backFemale)
        loadImage(pokemonData.sprites.backShinyFemale, binding.backShinyFemale)
        binding.images.post { adjustImagesWidth() }
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.images.rowCount = 2
            binding.images.columnCount = 4
        } else {
            binding.images.rowCount = 1
            binding.images.columnCount = 8
        }
    }

    private fun adjustImagesWidth() {
        val layoutWidth = binding.images.width
        fun check(imageView: ImageView) = if (imageView.visibility == View.GONE) 0 else imageView.width
        var imagesWidth = check(binding.frontDefault) + check(binding.frontShiny) + check(binding.frontFemale) +
                    check(binding.frontShinyFemale)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            imagesWidth += check(binding.backDefault) + check(binding.backShiny) + check(binding.backFemale) +
                    check(binding.backShinyFemale)
        if (imagesWidth > layoutWidth) {
            imagesWidth = layoutWidth /
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 4 else 8
            val setLayoutParams = { image: ImageView ->
                if (image.visibility == View.INVISIBLE)
                    image.layoutParams.apply {
                        width = imagesWidth
                        image.layoutParams = this
                    }
            }
            binding.frontDefault.run(setLayoutParams)
            binding.frontShiny.run(setLayoutParams)
            binding.frontFemale.run(setLayoutParams)
            binding.frontShinyFemale.run(setLayoutParams)
            binding.backDefault.run(setLayoutParams)
            binding.backShiny.run(setLayoutParams)
            binding.backFemale.run(setLayoutParams)
            binding.backShinyFemale.run(setLayoutParams)
        }
        if (binding.frontDefault.visibility == View.INVISIBLE) binding.frontDefault.visibility = View.VISIBLE
        if (binding.frontShiny.visibility == View.INVISIBLE) binding.frontShiny.visibility = View.VISIBLE
        if (binding.frontFemale.visibility == View.INVISIBLE) binding.frontFemale.visibility = View.VISIBLE
        if (binding.frontShinyFemale.visibility == View.INVISIBLE) binding.frontShinyFemale.visibility = View.VISIBLE
        if (binding.backDefault.visibility == View.INVISIBLE) binding.backDefault.visibility = View.VISIBLE
        if (binding.backShiny.visibility == View.INVISIBLE) binding.backShiny.visibility = View.VISIBLE
        if (binding.backFemale.visibility == View.INVISIBLE) binding.backFemale.visibility = View.VISIBLE
        if (binding.backShinyFemale.visibility == View.INVISIBLE) binding.backShinyFemale.visibility = View.VISIBLE
    }

}