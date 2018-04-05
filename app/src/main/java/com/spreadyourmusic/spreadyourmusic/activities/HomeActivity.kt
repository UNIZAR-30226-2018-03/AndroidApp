package com.spreadyourmusic.spreadyourmusic.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.media.session.MediaControllerCompat

import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager
import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.*
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    // Controla los cambios entre fragmentos
    private var actualFragmentDisplayed: Int = -1
    private val FRAGMENT_HOME_ID: Int = 0
    private val FRAGMENT_TREND_ID: Int = 1
    private val FRAGMENT_NEWS_ID: Int = 2
    private val FRAGMENT_BROWSER_ID: Int = 3
    private val FRAGMENT_GENRES_ID: Int = 4

    // Almacena el ultimo fragmento antes de abrir el browser
    private var beforeBrowserOpenID: Int = -1

    // Almacena los fragmentos para evitar recalcularlos
    private val fragmentHashMap: HashMap<Int, Fragment> = HashMap<Int, Fragment>(3)

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

        val actualFragmentToDisplay = getFragmentFromID(FRAGMENT_HOME_ID)

        if (actualFragmentToDisplay != null) {
            changeActualFragment(actualFragmentToDisplay)
            actualFragmentDisplayed = FRAGMENT_HOME_ID
            beforeBrowserOpenID = -1
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
                        val queryResults = obtainResultFromQuery(query)
                        val fragmento: Fragment? = BrowserFragment.newInstance(onRecomendationSelected, queryResults)

                        if (fragmento != null) {
                            beforeBrowserOpenID = actualFragmentDisplayed
                            actualFragmentDisplayed = FRAGMENT_BROWSER_ID
                            changeActualFragment(fragmento)
                        }
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
        val previousFragment = getFragmentFromID(beforeBrowserOpenID)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (previousFragment != null) {
            changeActualFragment(previousFragment)
            actualFragmentDisplayed = beforeBrowserOpenID
            beforeBrowserOpenID = -1
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
            searchView!!.clearFocus()
        } else if (!searchView!!.isIconified) {
            searchView!!.setQuery("", false)
            searchView!!.isIconified = true
            searchView!!.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if ((item.itemId == R.id.trends && actualFragmentDisplayed == FRAGMENT_TREND_ID) ||
                (item.itemId == R.id.home && actualFragmentDisplayed == FRAGMENT_HOME_ID) ||
                (item.itemId == R.id.news && actualFragmentDisplayed == FRAGMENT_NEWS_ID) ||
                (item.itemId == R.id.genres && actualFragmentDisplayed == FRAGMENT_GENRES_ID)) {
            drawer_layout.closeDrawer(GravityCompat.START)
            return true
        }

        // Handle navigation view item clicks
        val fragmento: Fragment? = when (item.itemId) {
            R.id.trends -> {
                actualFragmentDisplayed = FRAGMENT_TREND_ID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed)
            }

            R.id.home -> {
                actualFragmentDisplayed = FRAGMENT_HOME_ID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed)
            }
            R.id.news -> {
                actualFragmentDisplayed = FRAGMENT_NEWS_ID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed)
            }
            R.id.genres -> {
                actualFragmentDisplayed = FRAGMENT_GENRES_ID
                beforeBrowserOpenID = -1
                getFragmentFromID(actualFragmentDisplayed)
            }
            else ->
                null
        }

        if (fragmento != null) {
            // Insert the fragment by replacing any existing fragment
            changeActualFragment(fragmento)
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

    private fun getFragmentFromID(id: Int): Fragment? {
        return when (id) {
            FRAGMENT_TREND_ID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    mList.add(Pair(resources.getString(R.string.popular_now), obtainTrendSongs()))
                    mList.add(Pair(resources.getString(R.string.popular_in_my_country), obtainTrendInMyCountry()))
                    mList.add(Pair(resources.getString(R.string.popular_in_the_world), obtainPopularSongs()))
                    fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                }
                fragmentHashMap[id]
            }

            FRAGMENT_HOME_ID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    mList.add(Pair(resources.getString(R.string.recommendations), obtainRecommendations()))
                    mList.add(Pair(resources.getString(R.string.news), obtainNewsSongs()))
                    mList.add(Pair(resources.getString(R.string.populars), obtainPopularSongs()))
                    fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                }
                fragmentHashMap[id]
            }

            FRAGMENT_NEWS_ID -> {
                if (!fragmentHashMap.containsKey(id)) {
                    val mList = ArrayList<Pair<String, List<Recommendation>>>()
                    mList.add(Pair(resources.getString(R.string.playlist_updates), obtainUpdatedPlaylists()))
                    mList.add(Pair(resources.getString(R.string.new_songs), obtainNewsSongs()))
                    fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, mList)
                }
                fragmentHashMap[id]
            }

            FRAGMENT_GENRES_ID -> {
                if (!fragmentHashMap.containsKey(id)) fragmentHashMap[id] = HomeBaseFragment.newInstance(onRecomendationSelected, obtainPopularByGenre())
                fragmentHashMap[id]
            }

            else ->
                null
        }
    }

    private val onRecomendationSelected: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongSelected(it, this)
            is User -> onUserSelected(it)
            is Playlist -> onPlaylistSelected(it)
        }
    }
}
