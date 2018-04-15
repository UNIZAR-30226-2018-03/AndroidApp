package com.spreadyourmusic.spreadyourmusic.adapters

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by abel
 * On 8/03/18.
 */
abstract class GeneralRecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
    abstract fun bind(obj: Any)

}