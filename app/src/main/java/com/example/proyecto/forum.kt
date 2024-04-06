package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class forum : androidx.appcompat.app.AppCompatActivity() {
    private lateinit var editTextPost: EditText
    private lateinit var linearLayoutPosts: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum)

        editTextPost = findViewById(R.id.editTextPost)
        linearLayoutPosts = findViewById(R.id.linearLayoutPosts)

        // Agregar publicación al hacer clic en el botón "Publicar"
        val buttonPost = findViewById<TextView>(R.id.buttonPost)
        buttonPost.setOnClickListener {
            val postText = editTextPost.text.toString()

            if (postText.isNotEmpty()) {
                addPost(postText)
                editTextPost.text.clear()
            }
        }

        val buttonVolver = findViewById<TextView>(R.id.buttonVolver)
        buttonVolver.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // Agregar algunas publicaciones de ejemplo
        addPost("¿Qué opinas sobre el cambio climático?")
        addPost("¿Cuál es tu película favorita?")
        addPost("¿Qué planes tienes para el fin de semana?")
    }

    private fun addPost(postText: String) {
        // Crear un nuevo TextView para la publicación
        val postView = TextView(this)
        postView.text = postText
        postView.textSize = 16f
        postView.setPadding(0, 16, 0, 16)

        // Agregar la vista de la publicación al LinearLayout
        linearLayoutPosts.addView(postView, 0)
    }
}