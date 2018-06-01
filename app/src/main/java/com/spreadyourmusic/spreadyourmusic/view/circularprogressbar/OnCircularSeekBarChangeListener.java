/*
 * Fichero importado de https://github.com/AbelChT/circular-music-progressbar
 */

package com.spreadyourmusic.spreadyourmusic.view.circularprogressbar;

/**
 * A callback that notifies clients when the progress level has been changed.
 * This includes changes that were initiated by the user
 * through a touch gesture or arrow key/trackball as well as changes
 * that were initiated programmatically.
 */
public interface OnCircularSeekBarChangeListener {

    /**
     * Notification that the progress level has changed.
     * Clients can use the fromUser parameter to distinguish user-initiated changes
     * from those that occurred programmatically.
     *
     * @param circularBar : The CircularBar whose progress has changed
     * @param progress    : The current progress level. This will be in the range min.
     * @param fromUser    : True if the progress change was initiated by the user.
     */
    void onProgressChanged(CircularMusicProgressBar circularBar, int progress, boolean fromUser);

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    void onStartTrackingTouch(CircularMusicProgressBar seekBar);

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    void onStopTrackingTouch(CircularMusicProgressBar seekBar);
}
