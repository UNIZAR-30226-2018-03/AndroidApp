package com.spreadyourmusic.spreadyourmusic.activities

import android.app.FragmentManager
import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import com.spreadyourmusic.spreadyourmusic.services.MusicService
import com.spreadyourmusic.spreadyourmusic.fragment.PlayerControlsFragment
import com.spreadyourmusic.spreadyourmusic.helpers.media.NetworkHelper
import com.spreadyourmusic.spreadyourmusic.R

/**
 * Base activity for activities that need to show a playback control fragment when media is playing.
 */
abstract class BaseActivity : AppCompatActivity() {

    private var mMediaBrowser: MediaBrowserCompat? = null
    private var mControlsFragment: PlayerControlsFragment? = null

    // Callback that ensures that we are showing the controls
    private val mMediaControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            if (shouldShowControls()) {
                showPlaybackControls()
            } else {
                hidePlaybackControls()
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (shouldShowControls()) {
                showPlaybackControls()
            } else {
                hidePlaybackControls()
            }
        }
    }

    private val mConnectionCallback = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {

            try {
                connectToSession(mMediaBrowser!!.sessionToken)
            } catch (e: RemoteException) {
                hidePlaybackControls()
            }

        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Connect a media browser just to get the media session token. There are other ways
        // this can be done, for example by sharing the session token directly.
        mMediaBrowser = MediaBrowserCompat(this,
                ComponentName(this, MusicService::class.java), mConnectionCallback, null)
    }

    override fun onStart() {
        super.onStart()
        mControlsFragment = supportFragmentManager.findFragmentById(R.id.fragment_player_controls) as PlayerControlsFragment

        hidePlaybackControls()

        mMediaBrowser!!.connect()
    }

    override fun onStop() {
        super.onStop()
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mMediaControllerCallback)
        mMediaBrowser!!.disconnect()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // If not handled by drawerToggle, home needs to be handled by returning to previous
        if (item != null && item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // Otherwise, it may return to the previous fragment stack
        val fragmentManager = fragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            // Lastly, it will rely on the system behavior for back
            super.onBackPressed()
        }
    }

    protected fun onMediaControllerConnected() {
        // empty implementation, can be overridden by clients.
    }

    protected fun showPlaybackControls() {
        if (NetworkHelper.isOnline(this)) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                    .show(mControlsFragment)
                    .commit()
        }
    }

    protected fun hidePlaybackControls() {
        supportFragmentManager.beginTransaction()
                .hide(mControlsFragment)
                .commit()
    }

    /**
     * Check if the MediaSession is active and in a "playback-able" state
     * (not NONE and not STOPPED).
     *
     * @return true if the MediaSession's state requires playback controls to be visible.
     */
    protected fun shouldShowControls(): Boolean {
        val mediaController = MediaControllerCompat.getMediaController(this)
        if (mediaController == null ||
                mediaController.metadata == null ||
                mediaController.playbackState == null) {
            return false
        }
        when (mediaController.playbackState.state) {
            PlaybackStateCompat.STATE_ERROR, PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> return false
            else -> return true
        }
    }

    @Throws(RemoteException::class)
    private fun connectToSession(token: MediaSessionCompat.Token) {
        val mediaController = MediaControllerCompat(this, token)
        MediaControllerCompat.setMediaController(this, mediaController)
        mediaController.registerCallback(mMediaControllerCallback)

        if (shouldShowControls()) {
            showPlaybackControls()
        } else {
            hidePlaybackControls()
        }

        if (mControlsFragment != null) {
            mControlsFragment!!.onConnected()
        }

        onMediaControllerConnected()
    }

}
