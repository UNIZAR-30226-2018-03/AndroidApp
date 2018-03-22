package com.spreadyourmusic.spreadyourmusic.media

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.LruCache

import com.spreadyourmusic.spreadyourmusic.helpers.media.BitmapHelper
import com.spreadyourmusic.spreadyourmusic.helpers.media.LogHelper

import java.io.IOException

/**
 * Implements a basic cache of album arts, with async loading support.
 */
class AlbumArtCache private constructor() {

    private val mCache: LruCache<String, Array<Bitmap>>

    init {
        // Holds no more than MAX_ALBUM_ART_CACHE_SIZE bytes, bounded by maxmemory/4 and
        // Integer.MAX_VALUE:
        val maxSize = Math.min(MAX_ALBUM_ART_CACHE_SIZE,
                Math.min(Integer.MAX_VALUE.toLong(), Runtime.getRuntime().maxMemory() / 4).toInt())
        mCache = object : LruCache<String, Array<Bitmap>>(maxSize) {
            override fun sizeOf(key: String, value: Array<Bitmap>): Int {
                return value[BIG_BITMAP_INDEX].byteCount + value[ICON_BITMAP_INDEX].byteCount
            }
        }
    }

    fun getBigImage(artUrl: String, imageFetchedListener: (String, Bitmap?) -> Unit){
        val result = mCache.get(artUrl)
        if(result != null)
            imageFetchedListener(artUrl, result[BIG_BITMAP_INDEX])
        else
            fetch(artUrl, imageFetchedListener, BIG_BITMAP_INDEX)
    }

    fun getIconImage(artUrl: String, imageFetchedListener: (String, Bitmap?) -> Unit){
        val result = mCache.get(artUrl)
        if(result != null)
            imageFetchedListener(artUrl, result[ICON_BITMAP_INDEX])
        else
            fetch(artUrl, imageFetchedListener, ICON_BITMAP_INDEX)
    }

    fun fetch(artUrl: String, imageFetchedListener: (String, Bitmap?) -> Unit, imageType: Int) {
        // WARNING: for the sake of simplicity, simultaneous multi-thread fetch requests
        // are not handled properly: they may cause redundant costly operations, like HTTP
        // requests and bitmap rescales. For production-level apps, we recommend you use
        // a proper image loading library, like Glide.
        val bitmap = mCache.get(artUrl)
        if (bitmap != null) {
            LogHelper.d(TAG, "getOrFetch: album art is in cache, using it", artUrl)
            listener.onFetched(artUrl, bitmap[BIG_BITMAP_INDEX], bitmap[ICON_BITMAP_INDEX])
            return
        }
        LogHelper.d(TAG, "getOrFetch: starting asynctask to fetch ", artUrl)

        object : AsyncTask<Void, Void, Array<Bitmap>>() {
            override fun doInBackground(objects: Array<Void>): Array<Bitmap>? {
                val bitmaps: Array<Bitmap>
                try {
                    val bitmap = BitmapHelper.fetchAndRescaleBitmap(artUrl,
                            MAX_ART_WIDTH, MAX_ART_HEIGHT)
                    val icon = BitmapHelper.scaleBitmap(bitmap,
                            MAX_ART_WIDTH_ICON, MAX_ART_HEIGHT_ICON)
                    bitmaps = arrayOf(bitmap, icon)
                    mCache.put(artUrl, bitmaps)
                } catch (e: IOException) {
                    return null
                }

                LogHelper.d(TAG, "doInBackground: putting bitmap in cache. cache size=" + mCache.size())
                return bitmaps
            }

            override fun onPostExecute(bitmaps: Array<Bitmap>?) {
                if (bitmaps == null) {
                    listener.onError(artUrl, IllegalArgumentException("got null bitmaps"))
                } else {
                    listener.onFetched(artUrl,
                            bitmaps[BIG_BITMAP_INDEX], bitmaps[ICON_BITMAP_INDEX])
                }
            }
        }.execute()
    }

    abstract class FetchListener {
        abstract fun onFetched(artUrl: String, bigImage: Bitmap, iconImage: Bitmap)
        fun onError(artUrl: String, e: Exception) {
            LogHelper.e(TAG, e, "AlbumArtFetchListener: error while downloading $artUrl")
        }
    }

    companion object {
        private val TAG = LogHelper.makeLogTag(AlbumArtCache::class.java)

        private val MAX_ALBUM_ART_CACHE_SIZE = 12 * 1024 * 1024  // 12 MB
        private val MAX_ART_WIDTH = 800  // pixels
        private val MAX_ART_HEIGHT = 480  // pixels

        // Resolution reasonable for carrying around as an icon (generally in
        // MediaDescription.getIconBitmap). This should not be bigger than necessary, because
        // the MediaDescription object should be lightweight. If you set it too high and try to
        // serialize the MediaDescription, you may get FAILED BINDER TRANSACTION errors.
        private val MAX_ART_WIDTH_ICON = 128  // pixels
        private val MAX_ART_HEIGHT_ICON = 128  // pixels

        private val BIG_BITMAP_INDEX = 0
        private val ICON_BITMAP_INDEX = 1

        val instance = AlbumArtCache()
    }
}
