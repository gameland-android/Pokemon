package com.test.pokemon.di

import com.test.pokemon.network.Api
import com.test.pokemon.MainDataFlow
import com.test.pokemon.Repository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val BASE_URL = "https://pokeapi.co/api/v2/"

val dataFlowModule = module {
    viewModel { MainDataFlow(get()) }
}

val repositoryModule = module {
    single { Repository(get()) }
}

val apiModule = module {
    single {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create()).build().create(
            Api::class.java)
    }
}