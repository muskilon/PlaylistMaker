package com.example.playlistmaker.playlist.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.medialibrary.domain.PlayList

class DiffUtilPlayListCallback(
    private val oldList: List<PlayList>,
    private val newList: List<PlayList>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, title, description, cover, _, trackCount) = oldList[oldPosition]
        val (_, titleNew, descriptionNew, coverNew, _, trackCountNew) = newList[newPosition]

        return title == titleNew && description == descriptionNew && cover == coverNew && trackCount == trackCountNew
    }
}