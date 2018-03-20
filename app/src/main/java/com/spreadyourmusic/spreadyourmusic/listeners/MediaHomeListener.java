package com.spreadyourmusic.spreadyourmusic.listeners;

import com.spreadyourmusic.spreadyourmusic.media.MediaBrowserProvider;
import com.spreadyourmusic.spreadyourmusic.models.Playlist;
import com.spreadyourmusic.spreadyourmusic.models.Song;
import com.spreadyourmusic.spreadyourmusic.models.User;

/**
 * Created by abel on 19/03/18.
 */
// Esta clase la ha de implementar la home activity y sus metodos serán llamados cuando sobre alguno de los fragmentos se
// pulse sobre un artista una playlist o una cancion
public interface MediaHomeListener extends MediaBrowserProvider {
    void onSongSelected(Song song);

    void onPlaylistSelected(Playlist playlist);

    void onUserSelected(User user);
}
