package com.spreadyourmusic.spreadyourmusic.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.spreadyourmusic.spreadyourmusic.R
import android.support.v7.widget.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.spreadyourmusic.spreadyourmusic.controller.*
import com.spreadyourmusic.spreadyourmusic.fragment.VerticalRecyclerViewFragment
import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.Recommendation
import com.spreadyourmusic.spreadyourmusic.models.Song
import com.spreadyourmusic.spreadyourmusic.models.User
import android.view.MenuItem

class UserActivity : BaseActivity() {
    var user: User? = null
    var followButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userId = intent.getStringExtra(resources.getString(R.string.user_id))
        user = obtainUserFromID(userId)

        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = user!!.name

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        viewPager.adapter = TabsAdapter(supportFragmentManager, this, user!!)
        tabLayout.setupWithViewPager(viewPager)

        val profileImage = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)
        Glide.with(this).load(user!!.pictureLocationUri).into(profileImage)

        val artistUsername = findViewById<TextView>(R.id.artistUsername)

        val sArtistUsername = "@" + user!!.username

        artistUsername.text = sArtistUsername

        val followers = findViewById<TextView>(R.id.numOfFollowersTextView)

        val sNumFollowers = obtainNumberOfFollowers(user!!).toString() + " " + resources.getString(R.string.followers)

        followers.text = sNumFollowers

        followButton = findViewById(R.id.followButton)

        if (!user!!.username.equals(obtainCurrentUser().username)) {
            followButton!!.text = if (!isFollowing(user!!)) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
        } else {
            followButton!!.text = resources.getString(R.string.edit)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        if (!user!!.username.equals(obtainCurrentUser().username)){
            mInflater.inflate(R.menu.menu_artist, menu)
            menu!!.findItem(R.id.facebook_account).isVisible = user!!.getFacebookAccountURL() != null
            menu.findItem(R.id.twitter_account).isVisible = user!!.getTwitterAccountURL() != null
            menu.findItem(R.id.instagram_account).isVisible = user!!.getInstagramAccountURL() != null
        }
        else
            mInflater.inflate(R.menu.menu_local_user, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item != null) {
            when (item.itemId) {
                R.id.share -> {
                    shareElement(user!!.getShareLink(), this)
                    true
                }
                R.id.twitter_account -> {
                    openArtistSocialMediaProfile(user!!.getTwitterAccountURL()!!,this)
                    true
                }
                R.id.facebook_account -> {
                    openArtistSocialMediaProfile(user!!.getFacebookAccountURL()!!,this)
                    true
                }
                R.id.instagram_account -> {
                    openArtistSocialMediaProfile(user!!.getInstagramAccountURL()!!,this)
                    true
                }
                R.id.add_song -> {
                    val intent = Intent(this, UploadSongActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.create_playlist -> {
                    val intent = Intent(this, CreatePlaylistActivity::class.java)
                    startActivity(intent)
                    true
                }
                //TODO : Añadir eliminar cuenta
                else -> super.onOptionsItemSelected(item)
            }
        } else super.onOptionsItemSelected(item)
    }

    fun onDoFollow(view: View) {
        if (!user!!.username.equals(obtainCurrentUser().username)) {
            changeFollowState(user!!, !isFollowing(user!!))
            followButton!!.text = if (!isFollowing(user!!)) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
        } else {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(resources.getString(R.string.user_id), user!!.username)
            startActivity(intent)
        }
    }

    private class TabsAdapter(fm: FragmentManager, activity: Activity, user: User) : FragmentPagerAdapter(fm) {
        val mActivity: Activity = activity
        val mUser: User = user
        val onRecomendationSelected: (Recommendation) -> Unit = {
            when (it) {
                is Song -> onSongSelected(it, activity)
                is User -> onUserSelected(it, activity)
                is Playlist -> onPlaylistSelected(it, activity)
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
