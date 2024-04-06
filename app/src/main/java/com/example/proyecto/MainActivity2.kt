package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class MainActivity2 : AppCompatActivity() {






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val botonVuelta = findViewById<TextView>(R.id.botonVuelta)
        botonVuelta.setOnClickListener{

            val intento: Intent = Intent(this, MainActivity::class.java)

            startActivity(intento)

            finish()

        }

        val botonForo = findViewById<TextView>(R.id.botonForo)
        botonForo.setOnClickListener{

            val intento: Intent = Intent(this, forum::class.java)

            startActivity(intento)

            finish()

        }


        val botonPer = findViewById<TextView>(R.id.botonPerfil)
        botonPer.setOnClickListener{

            val intento: Intent = Intent(this, perfil::class.java)

            startActivity(intento)

            finish()

        }

        val mapButton = findViewById<TextView>(R.id.MapBoton)
        mapButton.setOnClickListener{

            val intento: Intent = Intent(this, MapActivity::class.java)

            startActivity(intento)

            finish()

        }

    }


}
