package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.media.session.MediaControllerCompat

import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.spreadyourmusic.spreadyourmusic.media.playback.QueueManager
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.fragment.BrowseFragment
import com.spreadyourmusic.spreadyourmusic.fragment.HomeFragment
import com.spreadyourmusic.spreadyourmusic.listeners.MediaHomeListener
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, MediaHomeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        changeActualFragment(HomeFragment.newInstance())
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        val fragmento: Fragment? = when (item.itemId) {
            R.id.browse ->
                BrowseFragment.newInstance()
            R.id.home ->
                HomeFragment.newInstance()
            else ->
                null
        }

        if (fragmento != null) {
            // Todo: Comprobar si el fragmento expandido no es el actual
            // Insert the fragment by replacing any existing fragment
            changeActualFragment(fragmento)
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun changeActualFragment(fragmento: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentForFragments, fragmento)
                .commit()
    }

    override fun onSongSelected(song: Song?) {
       // val intent = Intent(context, PlayerActivity::class.java)
        //startActivity(intent)
        //TODO: Abrir fullscreen
        val mediaController = MediaControllerCompat.getMediaController(this)
        QueueManager.getInstance().setCurrentQueue("Prueba",song)
        mediaController.transportControls
                .playFromMediaId(song!!.getMediaItem().getMediaId(), null)
    }

    override fun onPlaylistSelected(playlist: Playlist?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUserSelected(user: User?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
