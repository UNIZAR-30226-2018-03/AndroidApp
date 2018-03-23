package com.spreadyourmusic.spreadyourmusic.models

/**
 * Created by abel
 * On 7/03/18.
 */
class Playlist(val name: String, val creator: User, val artLocationUri: String, val content: List<Song>) : Recommendation