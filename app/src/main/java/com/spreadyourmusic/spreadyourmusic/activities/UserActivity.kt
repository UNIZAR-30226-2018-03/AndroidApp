package com.spreadyourmusic.spreadyourmusic.activities

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.spreadyourmusic.spreadyourmusic.R
import android.support.v7.widget.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.view.ContextMenu
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
import android.widget.Toast


class UserActivity : BaseActivity() {
    var user: User? = null
    var followButton: Button? = null
    private var mMenu: Menu? = null
    private var mTabsAdapter: TabsAdapter? = null

    // Esta variable almacenan si una canción o una playlist ha sido seleccionada con un long click
    private var itemSelectedToDelete: Recommendation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userId = intent.getStringExtra(resources.getString(R.string.user_id))

        //App bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        val profileImage = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)
        val artistUsername = findViewById<TextView>(R.id.artistUsername)
        val followers = findViewById<TextView>(R.id.numOfFollowersTextView)
        followButton = findViewById(R.id.followButton)
        followButton!!.setOnClickListener{
            onDoFollow()
        }

        obtainUserFromID(userId, this, {
            if (it != null) {
                user = it
                supportActionBar!!.title = it.name
                obtainSongsFromUser(it, this, {
                    val songList = it
                    obtainPlaylistsFromUser(user!!, this, {
                        val playlistList = it
                        mTabsAdapter  = TabsAdapter(supportFragmentManager, this, songList!!, playlistList!!, onRecomendationSelectedClickListener, onLongClickListenerDeleteElement)
                        viewPager.adapter = mTabsAdapter
                        tabLayout.setupWithViewPager(viewPager)
                    })
                })
                Glide.with(this).load(it.pictureLocationUri).into(profileImage)
                val sArtistUsername = "@" + it.username
                artistUsername.text = sArtistUsername
                obtainNumberOfFollowers(it, this, {
                    val sNumFollowers = it.toString() + " " + resources.getString(R.string.followers)
                    followers.text = sNumFollowers
                })
                obtainCurrentUserData({
                    if (!user!!.username.equals(it!!.username)) {
                        isFollowing(user!!, this, {
                            followButton!!.text = if (!it) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
                        })
                    } else {
                        followButton!!.text = resources.getString(R.string.edit)
                    }
                }, this)
            } else {
                Toast.makeText(this, "Error usuario no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }

            if (mMenu != null) {
                // Los datos del usuario no habian llegado aun cuando se creo el menu
                onCreateOptionsMenu(mMenu)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val mInflater = menuInflater
        obtainCurrentUserData({
            if (user != null) {
                if (!user!!.username.equals(it!!.username)) {
                    mInflater.inflate(R.menu.menu_artist, menu)
                    menu!!.findItem(R.id.facebook_account).isVisible = user!!.getFacebookAccountURL() != null
                    menu.findItem(R.id.twitter_account).isVisible = user!!.getTwitterAccountURL() != null
                    menu.findItem(R.id.instagram_account).isVisible = user!!.getInstagramAccountURL() != null
                } else
                    mInflater.inflate(R.menu.menu_local_user, menu)
            } else {
                mMenu = menu
            }
        }, this)
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
                    openArtistSocialMediaProfile(user!!.getTwitterAccountURL()!!, this)
                    true
                }
                R.id.facebook_account -> {
                    openArtistSocialMediaProfile(user!!.getFacebookAccountURL()!!, this)
                    true
                }
                R.id.instagram_account -> {
                    openArtistSocialMediaProfile(user!!.getInstagramAccountURL()!!, this)
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
                R.id.delete_account -> {
                    onDeleteAccount()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_context_local_user, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return if (item != null && item.itemId == R.id.delete_item && itemSelectedToDelete != null) {
            when (itemSelectedToDelete) {
                is Song -> onDeleteSong(itemSelectedToDelete!! as Song)
                is Playlist -> onDeletePlaylist(itemSelectedToDelete!! as Playlist)
            }
            true
        } else
            super.onContextItemSelected(item)
    }

    private fun onDoFollow() {
        obtainCurrentUserData({
            if (!user!!.username.equals(it!!.username)) {
                isFollowing(user!!, this, {
                    val anterior = it
                    changeFollowState(user!!, !it, this, {
                        if (it)
                            followButton!!.text = if (anterior) resources.getString(R.string.follow) else resources.getString(R.string.unfollow)
                    })
                })
            } else {
                val intent = Intent(this, SignUpActivity::class.java)
                intent.putExtra(resources.getString(R.string.user_id), user!!.username)
                startActivity(intent)
            }
        }, this)

    }

    private fun onDeleteAccount() {
        val builder = AlertDialog.Builder(this)

        builder.setPositiveButton(R.string.confirm, { _: DialogInterface?, _: Int ->
            if (doDeleteAccount(this)) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton(R.string.cancel, { _: DialogInterface?, _: Int ->
        })

        // Set other dialog properties
        builder.setTitle(R.string.delete_account)
        builder.setMessage(R.string.delete_account_dialog)


        // Create the AlertDialog
        builder.create()
        builder.show()
    }

    private fun onDeleteSong(song: Song) {
        val builder = AlertDialog.Builder(this)

        builder.setPositiveButton(R.string.confirm, { _: DialogInterface?, _: Int ->
            doDeleteSong(song, this, {
                if (it) {
                    obtainSongsFromUser(user!!, this, {
                        mTabsAdapter!!.songsListFragment.changeData(it)
                    })
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            })
        })

        builder.setNegativeButton(R.string.cancel, { _: DialogInterface?, _: Int ->
        })

        // Set other dialog properties
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.delete_song_dialog)

        // Create the AlertDialog
        builder.create()
        builder.show()
    }

    private fun onDeletePlaylist(playlist: Playlist) {
        val builder = AlertDialog.Builder(this)

        builder.setPositiveButton(R.string.confirm, { _: DialogInterface?, _: Int ->
            doDeletePlaylist(playlist, this, {
                if (it) {
                        obtainPlaylistsFromUser(user!!, this, {
                            mTabsAdapter!!.playlistListFragment.changeData(it)
                        })
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            })
        })

        builder.setNegativeButton(R.string.cancel, { _: DialogInterface?, _: Int ->
        })

        // Set other dialog properties
        builder.setTitle(R.string.delete)
        builder.setMessage(R.string.delete_playlist_dialog)

        // Create the AlertDialog
        builder.create()
        builder.show()
    }

    private val onRecomendationSelectedClickListener: (Recommendation) -> Unit = {
        when (it) {
            is Song -> onSongSelected(it, this)
            is User -> onUserSelected(it, this)
            is Playlist -> onPlaylistSelected(it, this)
        }
    }

    private val onLongClickListenerDeleteElement: (Recommendation, View?) -> Unit = { recommendation: Recommendation, view: View? ->
        obtainCurrentUserData({
            if (!user!!.username.equals(it!!.username) && (recommendation is Song || recommendation is Playlist)) {
                registerForContextMenu(view)
                itemSelectedToDelete = recommendation
                openContextMenu(view)
                unregisterForContextMenu(view)
            }
        }, this)
    }


    private class TabsAdapter(fm: FragmentManager, activity: Activity, songList: List<Recommendation>, playlistList: List<Playlist>, clickListener: (Recommendation) -> Unit, longClickListener: (Recommendation, View?) -> Unit) : FragmentPagerAdapter(fm) {
        val mActivity: Activity = activity

        val songsListFragment = VerticalRecyclerViewFragment.newInstance(clickListener, longClickListener, songList)
        val playlistListFragment = VerticalRecyclerViewFragment.newInstance(clickListener, longClickListener, playlistList)

        override fun getCount(): Int {
            return 2
        }

        override fun getItem(i: Int): Fragment {
            return when (i) {
                0 -> songsListFragment
                else -> playlistListFragment
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
