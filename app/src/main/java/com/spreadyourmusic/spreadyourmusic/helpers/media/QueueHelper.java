package com.spreadyourmusic.spreadyourmusic.helpers.media;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Pair;

import com.spreadyourmusic.spreadyourmusic.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to help on queue related tasks.
 */
public class QueueHelper {

    public static int getMusicIndexOnQueue(Iterable<Pair<Song, MediaSessionCompat.QueueItem>> queue,
                                           String mediaId) {
        int index = 0;
        for (Pair<Song, MediaSessionCompat.QueueItem> item : queue) {
            if (mediaId.equals(item.second.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static int getMusicIndexOnQueue(Iterable<Pair<Song, MediaSessionCompat.QueueItem>> queue,
                                           long queueId) {
        int index = 0;
        for (Pair<Song, MediaSessionCompat.QueueItem> item : queue) {
            if (queueId == item.second.getQueueId()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static List<Pair<Song, MediaSessionCompat.QueueItem>> convertToQueue(
            Iterable<Song> tracks) {
        List<Pair<Song, MediaSessionCompat.QueueItem>> queue = new ArrayList<>();
        int count = 0;
        for (Song track : tracks) {

            // We create a hierarchy-aware mediaID, so we know what the queue is about by looking
            // at the QueueItem media IDs.
            String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                    track.getMetadata().getDescription().getMediaId());

            MediaMetadataCompat trackCopy = new MediaMetadataCompat.Builder(track.getMetadata())
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                    .build();

            // We don't expect queues to change after created, so we use the item index as the
            // queueId. Any other number unique in the queue would work.
            MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(
                    trackCopy.getDescription(), count++);
            queue.add(new Pair<Song, MediaSessionCompat.QueueItem>(track, item));
        }
        return queue;

    }

    public static boolean isIndexPlayable(int index, List<Pair<Song, MediaSessionCompat.QueueItem>> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }
}
