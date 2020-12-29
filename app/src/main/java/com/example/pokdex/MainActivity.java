package com.example.pokdex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.pokdex.Adapters.PokemonAdapterList;
import com.example.pokdex.Config.Config;
import com.example.pokdex.Interface.PokeAPIService;
import com.example.pokdex.Models.Pokemon;
import com.example.pokdex.Models.ResponsePokemon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PokeAPIService pokeService = Config.getRetrofit().create(PokeAPIService.class);
    private RecyclerView recyclerView;
    private PokemonAdapterList pokemonAdapterList;
    private int offSet;
    private boolean readyToLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        pokemonAdapterList = new PokemonAdapterList(this);
        recyclerView.setAdapter(pokemonAdapterList);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy >0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if(readyToLoad) {
                        if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                            Log.i("INFO","LLEGAMOS AL FINAL");
                            readyToLoad = false;
                            offSet += 20;
                            loadData(offSet);
                        }
                    }
                }
            }
        });
        offSet = 0;
        loadData(offSet);
    }
    private void loadData(int offSet){
        Call<ResponsePokemon> responsePokemonCall = pokeService.getPokemonList(20, offSet);
        responsePokemonCall.enqueue(new Callback<ResponsePokemon>() {
            @Override
            public void onResponse(Call<ResponsePokemon> call, Response<ResponsePokemon> response) {
                readyToLoad = true;
                if(response.isSuccessful()) {
                    ResponsePokemon responsePokemon = response.body();
                    ArrayList<Pokemon> pokemonArrayList = responsePokemon.getResults();
                    pokemonAdapterList.AddList(pokemonArrayList);
                }
                else
                {
                    Log.d("ERROR", response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<ResponsePokemon> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}