package com.spreadyourmusic.spreadyourmusic.media.playback;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Pair;

import com.spreadyourmusic.spreadyourmusic.helpers.media.MediaIDHelper;
import com.spreadyourmusic.spreadyourmusic.helpers.media.QueueHelper;
import com.spreadyourmusic.spreadyourmusic.models.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple data provider for queues. Keeps track of a current queue and a current index in the
 * queue. Also provides methods to set the current queue based on common queries, relying on a
 * given MusicProvider to provide the actual media metadata.
 */
public class MusicQueueManager {

    private MetadataUpdateListener mListener;

    // "Now playing" queue:
    private List<Pair<Song, MediaSessionCompat.QueueItem>> mPlayingQueue;

    private int mCurrentIndex;

    private static MusicQueueManager instancia = null;


    private MusicQueueManager() {
        mPlayingQueue = Collections.synchronizedList(new ArrayList<Pair<Song, MediaSessionCompat.QueueItem>>());
        mCurrentIndex = 0;
    }


    private synchronized static void createInstance() {
        if (instancia == null) {
            instancia = new MusicQueueManager();
        }
    }

    public static MusicQueueManager getInstance() {
        if (instancia == null) {
            createInstance();
        }
        return instancia;
    }

    public void setListener(@NonNull MetadataUpdateListener listener) {
        this.mListener = listener;
    }

    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public boolean setCurrentQueueItem(long queueId) {
        // set the current index on queue from the queue Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean setCurrentQueueItem(String mediaId) {
        // set the current index on queue from the music Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean skipQueuePosition(int amount) {
        int index = mCurrentIndex + amount;
        if (index < 0) {
            // skip backwards before the first song will keep you on the first song
            index = 0;
        } else {
            // skip forwards when in last song will cycle back to start of the queue
            index %= mPlayingQueue.size();
        }
        if (!QueueHelper.isIndexPlayable(index, mPlayingQueue)) {
            return false;
        }
        mCurrentIndex = index;
        return true;
    }

    public MediaSessionCompat.QueueItem getCurrentMusic() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex).second;
    }

    public MediaMetadataCompat getCurrentMusicMetadata() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex).first.getMetadata();
    }

    public Song getCurrentSong() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex).first;
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    public void setCurrentQueue(String title, Song newSong) {
        List<Song> canciones = new ArrayList<Song>();
        canciones.add(newSong);
        setCurrentQueue(title, canciones);
    }

    public void setCurrentQueue(String title, List<Song> newSongList) {
        mPlayingQueue = QueueHelper.convertToQueue(newSongList);
        mCurrentIndex = 0;
        ArrayList<MediaSessionCompat.QueueItem> newQueue = new ArrayList<MediaSessionCompat.QueueItem>();
        for (Pair<Song, MediaSessionCompat.QueueItem> item : mPlayingQueue) {
            newQueue.add(item.second);
        }
        updateMetadata();
        mListener.onQueueUpdated(title, newQueue);
    }

    public List<MediaBrowserCompat.MediaItem> getCurrentMediaItemList() {

        List<MediaBrowserCompat.MediaItem> list = new ArrayList<>(mPlayingQueue.size());
        for (Pair<Song, MediaSessionCompat.QueueItem> item : mPlayingQueue) {
            list.add(item.first.getMediaItem());
        }
        return list;
    }



    public void updateMetadata() {
        MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null || currentMusic.getDescription() == null || currentMusic.getDescription().getMediaId() == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        final String musicId = MediaIDHelper.extractMusicIDFromMediaID(
                currentMusic.getDescription().getMediaId());
        MediaMetadataCompat metadata = getCurrentMusicMetadata();
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid musicId " + musicId);
        }

        mListener.onMetadataChanged(metadata);

    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);

        void onMetadataRetrieveError();

        void onCurrentQueueIndexUpdated(int queueIndex);

        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
