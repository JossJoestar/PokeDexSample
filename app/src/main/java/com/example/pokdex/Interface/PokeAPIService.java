package com.example.pokdex.Interface;

import com.example.pokdex.Models.ResponsePokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeAPIService {

    @GET("pokemon")
    Call<ResponsePokemon> getPokemonList(@Query("limint") int limit, @Query("offset") int offset);

}
