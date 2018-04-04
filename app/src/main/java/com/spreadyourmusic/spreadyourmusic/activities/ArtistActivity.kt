package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_artist.*
import com.spreadyourmusic.spreadyourmusic.R
import android.R
import android.support.v7.widget.*


class ArtistActivity : AppCompatActivity() {
    private val lstLista: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        //App bar
        val toolbar = findViewById(R.id.appbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Mi Aplicación")

        //RecyclerView
        val lstLista = findViewById(R.id.lstLista) as RecyclerView

        val datos = ArrayList()
        for (i in 0..49)
            datos.add(Titular("Título $i", "Subtítulo item $i"))

        val adaptador = AdaptadorTitulares(datos)
        lstLista.adapter = adaptador

        lstLista.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        lstLista.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        lstLista.itemAnimator = DefaultItemAnimator()
    }

}
