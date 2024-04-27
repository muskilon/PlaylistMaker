package com.example.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.MyApplication
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSinglePlaylistBinding
import com.example.playlistmaker.playlist.domain.SinglePlayListState
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SinglePlayListFragment : Fragment() {
    private var _binding: FragmentSinglePlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var tracksResultsAdapter: SinglePlayListAdapter
    private val viewModel by viewModel<SinglePlayListViewModel>()
    private var playListIsEmpty = true
    private var isClickAllowed = true
    private var bundleForEdit = bundleOf()
    private var bottomSheetPlayListMenuBehavior = BottomSheetBehavior<LinearLayout>()
    private var playListBottomSheetBehavior = BottomSheetBehavior<LinearLayout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSinglePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.arrowBack.setOnClickListener { exit() }

        viewModel.getPlayList(requireArguments().getLong(PLAYLIST))

        playListBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistBottomSheet)

        bottomSheetPlayListMenuBehavior = BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu)

        bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetPlayListMenuBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
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
            bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlayListDialog()
        }
        binding.share.setOnClickListener { sharePlaylist(it) }
        binding.sharePlaylistFromMenu.setOnClickListener { sharePlaylist(it) }
        binding.editInformation.setOnClickListener {
            findNavController().navigate(
                R.id.action_singlePlayListFragment_to_editPlayListFragment, bundleForEdit
            )
        }

        tracksResultsAdapter = SinglePlayListAdapter { track, longClick ->
            when (longClick) {
                false -> onClick(track)
                true -> onLongClick(track.trackId)
            }
        }
        binding.playListTracksRecycler.adapter = tracksResultsAdapter

        viewModel.getState().observe(viewLifecycleOwner) { state ->
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

    private fun sharePlaylist(view: View) {
        if (playListIsEmpty) {
            Snackbar.make(
                view, getString(R.string.there_is_tracks_to_share), Snackbar.LENGTH_LONG
            ).show()
        } else viewModel.sharePlayList()
        if (view === binding.sharePlaylistFromMenu) {
            bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
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
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.do_you_want_delete_track))
            .setMessage(getString(R.string.are_you_sure_to_delete_track))
            .setNegativeButton(getString(R.string.play_list_dialog_negative)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteTrackFromPlayList(trackId)
            }.show()
    }

    private fun deletePlayListDialog() {
        MaterialAlertDialogBuilder(requireContext()).setMessage(getString(R.string.are_you_sure_to_delete_playlist))
            .setTitle(getString(R.string.delete_playlist))
            .setNegativeButton(getString(R.string.play_list_dialog_negative)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deletePlayList()
                exit()
            }.show()
    }

    private fun setValues(state: SinglePlayListState) {
        tracksResultsAdapter.setData(state.currentPlayListTracks)
        if (state.currentPlayList.trackCount > 0) {
            playListIsEmpty = false
            binding.playListTracksRecycler.isVisible = true
            binding.emptyPlaylist.isVisible = false
        } else {
            playListIsEmpty = true
            binding.playListTracksRecycler.isVisible = false
            binding.emptyPlaylist.isVisible = true
        }
        binding.length.text = MyApplication.getAppResources()
            .getQuantityString(R.plurals.minutes_plurals, state.totalTime, state.totalTime)
        binding.trackCount.text = MyApplication.getAppResources().getQuantityString(
            R.plurals.track_plurals,
            state.currentPlayList.trackCount,
            state.currentPlayList.trackCount
        )
        with(binding.description) {
            text = state.currentPlayList.description
            isVisible = state.currentPlayList.description?.let { true } ?: false
        }
        binding.title.text = state.currentPlayList.title
        state.currentPlayList.cover?.let { binding.cover.setImageURI(it) }
            ?: binding.cover.setImageResource(R.drawable.placeholder)

        val screenHeight = binding.root.measuredHeight

        binding.shareLine.doOnNextLayout {
            playListBottomSheetBehavior.setPeekHeight(
                screenHeight - binding.shareLine.bottom, true
            )
            bottomSheetPlayListMenuBehavior.setPeekHeight(
                screenHeight - binding.titleLine.bottom, true
            )
        }
    }

    private fun setValuesForSummary(state: SinglePlayListState) {
        binding.menuSummary.playListTitle.text = state.currentPlayList.title
        binding.menuSummary.playListTrackCount.text =
            MyApplication.getAppResources().getQuantityString(
                R.plurals.track_plurals,
                state.currentPlayList.trackCount,
                state.currentPlayList.trackCount
            )
        state.currentPlayList.cover?.let { binding.menuSummary.playListCover.setImageURI(it) }
            ?: binding.menuSummary.playListCover.setImageResource(R.drawable.placeholder)
    }

    private fun exit() {
        when (bottomSheetPlayListMenuBehavior.state) {
            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED -> {
                bottomSheetPlayListMenuBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            else -> findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlayList(requireArguments().getLong(PLAYLIST))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        const val PLAYLIST = "playlist"
    }
}