package com.spreadyourmusic.spreadyourmusic.activities

import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.AppCompatActivity

import com.spreadyourmusic.spreadyourmusic.services.MusicService
import com.spreadyourmusic.spreadyourmusic.fragment.PlayerControlsFragment
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

        // Connect a media browser just to get the media session token (it's not the same that
        // back-end session token).
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

    protected fun showPlaybackControls() {
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                        R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                .show(mControlsFragment)
                .commit()
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
        return if (mediaController == null ||
                mediaController.metadata == null ||
                mediaController.playbackState == null) {
            false
        } else {
            when (mediaController.playbackState.state) {
                PlaybackStateCompat.STATE_ERROR, PlaybackStateCompat.STATE_NONE, PlaybackStateCompat.STATE_STOPPED -> false
                else -> true
            }
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

    }

}
