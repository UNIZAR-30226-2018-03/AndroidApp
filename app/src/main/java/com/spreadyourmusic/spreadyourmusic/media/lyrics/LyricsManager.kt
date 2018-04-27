package com.spreadyourmusic.spreadyourmusic.media.lyrics

object LyricsManager {
    private var mListener: ((String) -> Unit)? = null

    fun changeListener(listener :((String) -> Unit)?){
        mListener = listener
    }

    fun onTextChange(text : String?){
        if(text != null && mListener != null)
            mListener?.invoke(text)
    }
}