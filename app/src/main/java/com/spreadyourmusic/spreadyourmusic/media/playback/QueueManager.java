package com.spreadyourmusic.spreadyourmusic.media.playback;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.spreadyourmusic.spreadyourmusic.media.AlbumArtCache;
import com.spreadyourmusic.spreadyourmusic.helpers.media.MediaIDHelper;
import com.spreadyourmusic.spreadyourmusic.helpers.media.QueueHelper;
import com.spreadyourmusic.spreadyourmusic.models.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Simple data provider for queues. Keeps track of a current queue and a current index in the
 * queue. Also provides methods to set the current queue based on common queries, relying on a
 * given MusicProvider to provide the actual media metadata.
 */
public class QueueManager {

    private MetadataUpdateListener mListener;

    // "Now playing" queue:
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private List<Song> mCurrentMetadata;
    private int mCurrentIndex;

    private static QueueManager instancia = null;


    private QueueManager() {
        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mCurrentMetadata = Collections.synchronizedList(new ArrayList<Song>());
        mCurrentIndex = 0;
    }


    private synchronized static void createInstance() {
        if (instancia == null) {
            instancia = new QueueManager();
        }
    }

    public static QueueManager getInstance(){
        if(instancia == null){
            createInstance();
        }
        return instancia;
    }

    public void setListener(@NonNull MetadataUpdateListener listener){
        this.mListener = listener;
    }

    public boolean isSameBrowsingCategory(@NonNull String mediaId) {
        String[] newBrowseHierarchy = MediaIDHelper.getHierarchy(mediaId);
        MediaSessionCompat.QueueItem current = getCurrentMusic();
        if (current == null) {
            return false;
        }
        String[] currentBrowseHierarchy = MediaIDHelper.getHierarchy(
                current.getDescription().getMediaId());

        return Arrays.equals(newBrowseHierarchy, currentBrowseHierarchy);
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
        return mPlayingQueue.get(mCurrentIndex);
    }

    public MediaMetadataCompat getCurrentMusicMetadata() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mCurrentMetadata.get(mCurrentIndex).getMetadata();
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    public void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue, List<Song> newSongList) {
        setCurrentQueue(title, newQueue, newSongList, null);
    }

    public void setCurrentQueue(String title, List<Song> newSongList) {
        setCurrentQueue(title, QueueHelper.convertToQueueFromSong(newSongList), newSongList, null);
    }

    public void setCurrentQueue(String title, Song newSong) {
        List<Song> canciones = new ArrayList<Song>();
        canciones.add(newSong);
        setCurrentQueue(title,canciones);
    }

    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue, List<Song> newSongList,
                                   String initialMediaId) {
        mPlayingQueue = newQueue;
        mCurrentMetadata = newSongList;
        int index = 0;
        if (initialMediaId != null) {
            index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
        updateMetadata();
        mListener.onQueueUpdated(title, newQueue);
    }

    public List<MediaBrowserCompat.MediaItem> getCurrentMediaItemList(){

        List<MediaBrowserCompat.MediaItem> lista = new ArrayList<>(mPlayingQueue.size());
        for (Song dato: mCurrentMetadata) {
            lista.add(dato.getMediaItem());
        }
        return lista;
    }

    public synchronized void updateMusicArt(int index, Bitmap albumArt, Bitmap icon) {
        MediaMetadataCompat metadata = mCurrentMetadata.get(index).getMetadata();
        metadata = new MediaMetadataCompat.Builder(metadata)

                // set high resolution bitmap in METADATA_KEY_ALBUM_ART. This is used, for
                // example, on the lockscreen background when the media session is active.
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)

                // set small version of the album art in the DISPLAY_ICON. This is used on
                // the MediaDescription and thus it should be small to be serialized if
                // necessary
                .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, icon)

                .build();

        mCurrentMetadata.get(index).setMetadata(metadata);

    }


    public void updateMetadata() {
        MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null) {
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
        final int lastIndex = mCurrentIndex;

        // Set the proper album artwork on the media session, so it can be shown in the
        // locked screen and in other places.
        if (metadata.getDescription().getIconBitmap() == null &&
                metadata.getDescription().getIconUri() != null) {
            String albumUri = metadata.getDescription().getIconUri().toString();
            AlbumArtCache.getInstance().fetch(albumUri, new AlbumArtCache.FetchListener() {
                @Override
                public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
                   updateMusicArt(lastIndex, bitmap, icon);

                    // If we are still playing the same music, notify the listeners:
                    MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
                    if (currentMusic == null) {
                        return;
                    }
                    String currentPlayingId = MediaIDHelper.extractMusicIDFromMediaID(
                            currentMusic.getDescription().getMediaId());
                    if (musicId.equals(currentPlayingId)) {
                        mListener.onMetadataChanged(getCurrentMusicMetadata());
                    }
                }
            });
        }
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
