package com.example.tulio

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonImageView: ImageView
    private lateinit var loadPokemonButton: Button
    private lateinit var searchByIdButton: Button
    private lateinit var pokemonNameTextView: TextView
    private lateinit var pokemonIdEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonImageView = findViewById(R.id.pokemonImageView)
        loadPokemonButton = findViewById(R.id.loadPokemonButton)
        searchByIdButton = findViewById(R.id.searchByIdButton)
        pokemonNameTextView = findViewById(R.id.pokemonNameTextView)
        pokemonIdEditText = findViewById(R.id.pokemonIdEditText)

        loadPokemonButton.setOnClickListener {
            loadRandomPokemon()
        }

        searchByIdButton.setOnClickListener {
            val input = pokemonIdEditText.text.toString()
            if (input.isNotEmpty()) {
                val id = input.toInt()
                if (id in 1..1025) {
                    loadPokemonById(id)
                } else {
                    Toast.makeText(this, "ID deve ser entre 1 e 1025", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Digite um ID", Toast.LENGTH_SHORT).show()
            }
        }

        loadRandomPokemon()
    }

    private fun loadRandomPokemon() {
        val randomId = (1..1025).random()
        loadPokemonById(randomId)
    }

    private fun loadPokemonById(id: Int) {
        val call = ApiClient.instance.getPokemon(id)
        call.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    pokemon?.let {
                        val imageUrl = it.sprites.front_default
                        val name = it.name.replaceFirstChar { c -> c.uppercase() }

                        pokemonNameTextView.text = name
                        Picasso.get().load(imageUrl).into(pokemonImageView)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Pokémon não encontrado", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
