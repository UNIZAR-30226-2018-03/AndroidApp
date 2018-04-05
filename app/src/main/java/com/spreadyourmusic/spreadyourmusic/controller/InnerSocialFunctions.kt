package com.spreadyourmusic.spreadyourmusic.controller

import com.spreadyourmusic.spreadyourmusic.models.Playlist
import com.spreadyourmusic.spreadyourmusic.models.User

/**
 * Created by abel on 8/03/18.
 */

// False si no true si si
fun isFollowing(playlist :Playlist):Boolean{
    // TODO:
    return false
}

// False dejar de seguir , true seguir
fun changeFollowState(playlist :Playlist, boolean: Boolean){
    // TODO:
}

// False si no true si si
fun isFollowing(playlist : User):Boolean{
    // TODO:
    return false
}

// False dejar de seguir , true seguir
fun changeFollowState(playlist :User, boolean: Boolean){
    // TODO:
}

fun obtainNumberOfFollowers(playlist :User):Int{
    return 17
}

fun obtainNumberOfFollowers(playlist :Playlist):Int{
    return 499
}