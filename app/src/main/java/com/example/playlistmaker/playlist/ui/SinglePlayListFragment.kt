package com.example.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.playlist.domain.SinglePlayListState
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SinglePlayListFragment : Fragment() {
    private lateinit var binding: FragmentSinglePlaylistBinding
    private lateinit var tracksResultsAdapter: SinglePlayListAdapter
    private val viewModel by viewModel<SinglePlayListViewModel>()
    private val playListTracks = ArrayList<Track>()
    private var isClickAllowed = true
    private var bundleForEdit = bundleOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSinglePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener { exit() }

        viewModel.getPlayList(requireArguments().getLong(PLAYLIST))

        val bottomSheetPlayListMenuBehavior =
            BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu)
        bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetPlayListMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {}

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.playlistBottomSheet.isVisible = false
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.playlistBottomSheet.isVisible = true
                    }

                    else -> {
                        Unit
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.menu.setOnClickListener {
            bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.deletePlaylist.setOnClickListener {
            deletePlayListDialog()
        }
        binding.share.setOnClickListener {
            viewModel.sharePlayList()
        }
        binding.sharePlaylistFromMenu.setOnClickListener {
            viewModel.sharePlayList()
        }
        binding.editInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_singlePlayListFragment_to_editPlayListFragment, bundleForEdit
            )
        }

        tracksResultsAdapter = SinglePlayListAdapter(playListTracks) { track, longClick ->
            when (longClick) {
                false -> onClick(track)
                true -> onLongClick(track.trackId)
            }
        }
        binding.playListTracksRecycler.adapter = tracksResultsAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            if (state.currentPlayList.trackCount > 0) {
                playListTracks.clear()
                playListTracks.addAll(state.currentPlayListTracks)
                tracksResultsAdapter.notifyDataSetChanged()
            } else {
                playListTracks.clear()
                tracksResultsAdapter.notifyDataSetChanged()
                //TODO показывать заглушку
            }
            setValues(state)
            setValuesForSummary(state)
            createArgs(state.currentPlayList.id)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exit()
                }
            })
    }

    private fun createArgs(playlistId: Long) {
        bundleForEdit = bundleOf(
            PLAYLIST to playlistId
        )
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun onClick(track: Track) {
        if (clickDebounce()) {
            viewModel.onTrackClick(track)
            findNavController().navigate(
                R.id.action_singlePlayListFragment_to_playerFragment
            )
        }
    }

    private fun onLongClick(trackId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.do_you_want_delete_track))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteTrackFromPlayList(trackId)
            }
            .show()
    }

    private fun deletePlayListDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.do_you_want_delete_playlist, binding.title.text))
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlayList()
                exit()
            }
            .show()
    }

    private fun setValues(state: SinglePlayListState) {
        binding.length.text = MyApplication.getAppResources()
            .getQuantityString(R.plurals.minutes_plurals, state.totalTime, state.totalTime)
        binding.trackCount.text = MyApplication.getAppResources()
            .getQuantityString(
                R.plurals.track_plurals,
                state.currentPlayList.trackCount,
                state.currentPlayList.trackCount
            )
        if (state.currentPlayList.description.isNullOrEmpty()) binding.description.isVisible = false
        binding.description.text = state.currentPlayList.description
        binding.title.text = state.currentPlayList.title
        Glide.with(binding.cover)
            .load(state.currentPlayList.cover)
            .placeholder(R.drawable.placeholder)
            .into(binding.cover)
    }

    private fun setValuesForSummary(state: SinglePlayListState) {
        binding.menuSummary.playListTitle.text = state.currentPlayList.title
        binding.menuSummary.playListTrackCount.text = MyApplication.getAppResources()
            .getQuantityString(
                R.plurals.track_plurals,
                state.currentPlayList.trackCount,
                state.currentPlayList.trackCount
            )
        Glide.with(binding.menuSummary.playListCover)
            .load(state.currentPlayList.cover)
            .placeholder(R.drawable.placeholder)
            .into(binding.menuSummary.playListCover)
    }

    private fun exit() {
        findNavController().navigateUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlayList(requireArguments().getLong(PLAYLIST))
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        const val PLAYLIST = "playlist"
    }
}