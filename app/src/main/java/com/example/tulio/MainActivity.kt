package com.example.tulio

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        lifecycleScope.launch {
            try {
                val pokemon = withContext(Dispatchers.IO) {
                    ApiClient.instance.getPokemon(id)
                }

                val imageUrl = pokemon.sprites.front_default
                val name = pokemon.name.replaceFirstChar { c -> c.uppercase() }

                pokemonNameTextView.text = name
                Picasso.get().load(imageUrl).into(pokemonImageView)

            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
