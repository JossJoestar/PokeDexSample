package com.example.pokdex.Models;

import java.util.ArrayList;

public class ResponsePokemon {
    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}
