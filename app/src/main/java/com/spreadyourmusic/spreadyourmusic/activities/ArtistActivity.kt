package com.spreadyourmusic.spreadyourmusic.activities

import android.app.Activity
import android.os.Bundle
import com.spreadyourmusic.spreadyourmusic.R
import android.support.v7.widget.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.view.View
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.VerticalRecyclerViewFragment
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User


class ArtistActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        val user = obtainUserFromID(23)

        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = user.name

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        viewPager.adapter = TabsAdapter(supportFragmentManager, this, user)
        tabLayout.setupWithViewPager(viewPager)
    }

    fun onDoFollow(view: View) {

    }

    private class TabsAdapter(fm: FragmentManager, activity: Activity, user: User) : FragmentPagerAdapter(fm) {
        val mActivity: Activity = activity
        val mUser: User = user
        val onRecomendationSelected: (Recommendation) -> Unit = {
            when (it) {
                is Song -> onSongSelected(it, activity)
                is User -> onUserSelected(it)
                is Playlist -> onPlaylistSelected(it)
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getItem(i: Int): Fragment {
            return when (i) {
                0 -> VerticalRecyclerViewFragment.newInstance(onRecomendationSelected, obtainSongsFromUser(mUser))
                else -> VerticalRecyclerViewFragment.newInstance(onRecomendationSelected, obtainPlaylistsFromUser(mUser))
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> mActivity.resources.getString(R.string.songs)
                1 -> mActivity.resources.getString(R.string.playlist)
                else -> null
            }
        }
    }
}
