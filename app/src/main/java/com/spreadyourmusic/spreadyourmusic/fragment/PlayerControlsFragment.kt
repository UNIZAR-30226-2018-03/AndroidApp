package com.spreadyourmusic.spreadyourmusic.fragment

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import com.spreadyourmusic.spreadyourmusic.R
import com.spreadyourmusic.spreadyourmusic.activities.PlayerActivity
import com.spreadyourmusic.spreadyourmusic.controller.getCurrentSong
import com.spreadyourmusic.spreadyourmusic.media.playback.MusicQueueManager

/**
 * Player buttons controller
 */
class PlayerControlsFragment : Fragment() {

    private var mPlayPause: ImageButton? = null
    private var mTitle: TextView? = null
    private var mSubtitle: TextView? = null
    private var nextSongImageButton:ImageButton? = null
    private var previousSongImageButton: ImageButton? = null

    private val mCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            this@PlayerControlsFragment.onPlaybackStateChanged(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            if (metadata == null) {
                return
            }
            this@PlayerControlsFragment.onMetadataChanged(metadata)
        }
    }

    private val mPlayPauseListener = View.OnClickListener { v ->
        val controller = MediaControllerCompat.getMediaController(activity!!)
        val stateObj = controller.playbackState
        val state = stateObj?.state ?: PlaybackStateCompat.STATE_NONE
        when (v.id) {
            R.id.playPauseSong -> if (state == PlaybackStateCompat.STATE_PAUSED ||
                    state == PlaybackStateCompat.STATE_STOPPED ||
                    state == PlaybackStateCompat.STATE_NONE) {
                playMedia()
            } else if (state == PlaybackStateCompat.STATE_PLAYING ||
                    state == PlaybackStateCompat.STATE_BUFFERING ||
                    state == PlaybackStateCompat.STATE_CONNECTING) {
                pauseMedia()
            }
        }
    }

    private val mNextSongListener = View.OnClickListener { _ ->
        val controls = MediaControllerCompat.getMediaController(activity!!).transportControls
        controls.skipToNext()
    }

    private val mPreviousSongListener = View.OnClickListener { _ ->
        val controls = MediaControllerCompat.getMediaController(activity!!).transportControls
        controls.skipToPrevious()
    }

    private val mBackgroundListener = View.OnClickListener { _ ->
        val intent = Intent(activity, PlayerActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_player_controls, container, false)
        mPlayPause = rootView.findViewById(R.id.playPauseSong)
        mPlayPause!!.isEnabled = true
        mPlayPause!!.setOnClickListener(mPlayPauseListener)

        mTitle = rootView.findViewById(R.id.songTitle)
        mSubtitle = rootView.findViewById(R.id.songAuthor)

        nextSongImageButton = rootView.findViewById(R.id.nextSong)
        nextSongImageButton!!.setOnClickListener(mNextSongListener)
        previousSongImageButton = rootView.findViewById(R.id.previousSong)
        previousSongImageButton!!.setOnClickListener(mPreviousSongListener)

        val backgroundLayout : View = rootView.findViewById(R.id.fondo)
        backgroundLayout.setOnClickListener(mBackgroundListener)
        mTitle!!.setOnClickListener(mBackgroundListener)
        mSubtitle!!.setOnClickListener(mBackgroundListener)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val controller = MediaControllerCompat.getMediaController(activity!!)
        if (controller != null) {
            onConnected()
        }
    }

    override fun onStop() {
        super.onStop()
        val controller = MediaControllerCompat.getMediaController(activity!!)
        controller?.unregisterCallback(mCallback)
    }

    fun onConnected() {
        val controller = MediaControllerCompat.getMediaController(activity!!)
        if (controller != null) {
            onMetadataChanged(controller.metadata)
            onPlaybackStateChanged(controller.playbackState)
            controller.registerCallback(mCallback)
        }
    }

    private fun onMetadataChanged(metadata: MediaMetadataCompat?) {
        if (activity != null && metadata != null) {
            val currentSong = getCurrentSong()
            mTitle!!.text = currentSong.name
            mSubtitle!!.text = currentSong.album.creator.username
            nextSongImageButton!!.visibility = if (MusicQueueManager.getInstance().currentQueueSize == 1)
                View.INVISIBLE
            else
                View.VISIBLE
            previousSongImageButton!!.visibility = nextSongImageButton!!.visibility
        }
    }

    private fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
        if (activity != null && state != null) {
            var enablePlay = false
            when (state.state) {
                PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.STATE_STOPPED -> enablePlay = true
                PlaybackStateCompat.STATE_ERROR -> Toast.makeText(activity, state.errorMessage, Toast.LENGTH_LONG).show()
            }

            if (enablePlay) {
                mPlayPause!!.setImageDrawable(
                        ContextCompat.getDrawable(activity!!, R.drawable.ic_play_circle_filled_white_24dp))
            } else {
                mPlayPause!!.setImageDrawable(
                        ContextCompat.getDrawable(activity!!, R.drawable.ic_pause_circle_filled_white_24dp))
            }
        }
    }

    private fun playMedia() {
        val controller = MediaControllerCompat.getMediaController(activity!!)
        controller?.transportControls?.play()
    }

    private fun pauseMedia() {
        val controller = MediaControllerCompat.getMediaController(activity!!)
        controller?.transportControls?.pause()
    }
}
