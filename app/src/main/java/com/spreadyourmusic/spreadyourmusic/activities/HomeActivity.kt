package com.spreadyourmusic.spreadyourmusic.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment

import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import com.spreadyourmusic.spreadyourmusic.session.SessionSingleton
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Controla los cambios entre fragmentos
    private var actualFragmentDisplayed: Int = -1
    private val fragmentHomeID: Int = 0
    private val fragmentTrendID: Int = 1
    private val fragmentNewsID: Int = 2
    private val fragmentBrowserID: Int = 3
    private val fragmentGenresID: Int = 4

    // Almacena el ultimo fragmento antes de abrir el browser
    private var beforeBrowserOpenID: Int = -1

    // Almacena los fragmentos para evitar recalcularlos
    private val fragmentHashMap: HashMap<Int, Fragment> = HashMap(3)

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        getFragmentFromID(fragmentHomeID, {
            changeActualFragment(it)
            actualFragmentDisplayed = fragmentHomeID
            beforeBrowserOpenID = -1
        })

        val hView = nav_view.getHeaderView(0)

        val circularImageView = hView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.coverCircleImageView)
        val userName = hView.findViewById<TextView>(R.id.usernameTextView)
        obtainCurrentUserData({
            val mmCurrentUser = it
            if (mmCurrentUser != null) {
                userName.text = mmCurrentUser.name
                circularImageView.setOnClickListener {
                    onUserSelected(mmCurrentUser, this)
                }
                Glide.with(this).load(mmCurrentUser.pictureLocationUri).into(circularImageView)
            } else {
                Toast.makeText(this, "Error: No se han podido obtener los datos de usuario", Toast.LENGTH_SHORT).show()
            }
        }, this)
    }

    override fun onStart() {
        super.onStart()
        val lastSongListened = SessionSingleton.lastSongListened
        if (lastSongListened != null) {
            SessionSingleton.lastSongListened = null
            onSongSelected(lastSongListened, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        mInflater.inflate(R.menu.menu_home, menu)

        if (menu != null) {
            searchView = menu.findItem(R.id.action_search).actionView as SearchView
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        obtainResultFromQuery(query, this@HomeActivity, {
                            val queryResults = it
                            if (queryResults != null) {
                                val fragmento: Fragment? = BrowserFragment.newInstance(onRecomendationSelected, queryResults)
                                if (fragmento != null) {
                                    beforeBrowserOpenID = actualFragmentDisplayed
                                    actualFragmentDisplayed = fragmentBrowserID
                                    changeActualFragment(fragmento)
                                }
                            } else Toast.makeText(this@HomeActivity, "Error", Toast.LENGTH_SHORT).show()
                        })
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (!searchView!!.isIconified) {
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
            searchView!!.clearFocus()
            if (beforeBrowserOpenID != -1) {
                getFragmentFromID(beforeBrowserOpenID, { changeActualFragment(it) })
                actualFragmentDisplayed = beforeBrowserOpenID
                beforeBrowserOpenID = -1
                searchView!!.setQuery("", false)
                searchView!!.isIconified = true
                searchView!!.clearFocus()
            }
        } else if (beforeBrowserOpenID != -1) {
            getFragmentFromID(beforeBrowserOpenID, { changeActualFragment(it) })
            actualFragmentDisplayed = beforeBrowserOpenID
            beforeBrowserOpenID = -1
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
            searchView!!.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == R.id.trends && actualFragmentDisplayed == fragmentTrendID) ||
                (item.itemId == R.id.home && actualFragmentDisplayed == fragmentHomeID) ||
                (item.itemId == R.id.news && actualFragmentDisplayed == fragmentNewsID) ||
                (item.itemId == R.id.genres && actualFragmentDisplayed == fragmentGenresID)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }

        // Handle navigation view item clicks
        when (item.itemId) {
            R.id.trends -> {
                actualFragmentDisplayed = fragmentTrendID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed, { changeActualFragment(it) })
            }

            R.id.home -> {
                actualFragmentDisplayed = fragmentHomeID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed, { changeActualFragment(it) })
            }
            R.id.news -> {
                actualFragmentDisplayed = fragmentNewsID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed, { changeActualFragment(it) })
            }
            R.id.genres -> {
                actualFragmentDisplayed = fragmentGenresID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed, { changeActualFragment(it) })
            }
            R.id.playlist -> onSystemListSelected(0, this)
            R.id.artist -> onSystemListSelected(1, this)
            R.id.songs -> onSystemListSelected(2, this)
            R.id.downloaded -> onSystemListSelected(3, this)
            R.id.open_profile -> {
                obtainCurrentUserData({
                    if (it != null)
                        onUserSelected(it, this)
                }, this)
            }
            R.id.close_session -> {
                doLogout(this)
                val int = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(int)
                finish()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changeActualFragment(fragmento: Fragment) {
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.contentForFragments, fragmento)
                .commit()
    }

    private fun getFragmentFromID(id: Int, listener: (Fragment) -> Unit) {
        when (id) {
            fragmentTrendID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    // Mientras se carga se muestra una pantalla en blanco
                    listener(HomeBaseFragment.newInstance(onRecomendationSelected, mList))

                    obtainTrendSongs(this, {
                        val trendSongs = it
                        obtainTrendInMyCountry(this, {
                            val trendInMyCountry = it
                            obtainPopularSongs(this, {
                                val popularSongs = it
                                if (popularSongs == null || trendInMyCountry == null || trendSongs == null) {
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                } else {
                                    mList.add(Pair(resources.getString(R.string.popular_now), trendSongs))
                                    mList.add(Pair(resources.getString(R.string.popular_in_my_country), trendInMyCountry))
                                    mList.add(Pair(resources.getString(R.string.popular_in_the_world), popularSongs))
                                    fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                                    listener(fragmentHashMap[id]!!)
                                }
                            })
                        })
                    })
                } else {
                    listener(fragmentHashMap[id]!!)
                }
            }

            fragmentHomeID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    // Mientras se carga se muestra una pantalla en blanco
                    listener(HomeBaseFragment.newInstance(onRecomendationSelected, mList))

                    obtainRecommendations(this, {
                        val recommendations = it
                        obtainNewsSongs(this, {
                            val newsSongs = it
                            obtainPopularSongs(this, {
                                val popularSongs = it
                                if (popularSongs == null || recommendations == null || newsSongs == null) {
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                } else {
                                    mList.add(Pair(resources.getString(R.string.recommendations), recommendations))
                                    mList.add(Pair(resources.getString(R.string.new_songs), newsSongs))
                                    mList.add(Pair(resources.getString(R.string.popular_in_the_world), popularSongs))
                                    fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                                    listener(fragmentHashMap[id]!!)
                                }
                            })
                        })
                    })
                } else {
                    listener(fragmentHashMap[id]!!)
                }
            }

            fragmentNewsID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    // Mientras se carga se muestra una pantalla en blanco
                    listener(HomeBaseFragment.newInstance(onRecomendationSelected, mList))

                    obtainUpdatedPlaylists(this, {
                        val updatedPlaylists = it
                        obtainNewsSongs(this, {
                            val newsSongs = it
                            if (updatedPlaylists == null || newsSongs == null) {
                                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                            } else {
                                mList.add(Pair(resources.getString(R.string.new_songs), newsSongs))
                                mList.add(Pair(resources.getString(R.string.playlist_updates), updatedPlaylists))
                                fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                                listener(fragmentHashMap[id]!!)
                            }
                        })
                    })
                } else {
                    listener(fragmentHashMap[id]!!)
                }
            }

            fragmentGenresID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    // Mientras se carga se muestra una pantalla en blanco
                    listener(HomeBaseFragment.newInstance(onRecomendationSelected, mList))

                    obtainPopularByGenre(this, {
                        val popularByGenre = it
                        if (popularByGenre == null) {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        } else {
                            fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, popularByGenre)
                            listener(fragmentHashMap[id]!!)
                        }
                    })
                } else {
                    listener(fragmentHashMap[id]!!)
                }
            }
        }
    }

    private val onRecomendationSelected: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongSelected(it, this)
            is User -> onUserSelected(it, this)
            is Playlist -> onPlaylistSelected(it, this)
        }
    }
}
