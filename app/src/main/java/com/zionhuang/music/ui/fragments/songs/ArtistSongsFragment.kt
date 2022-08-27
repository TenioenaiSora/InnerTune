package com.zionhuang.music.ui.fragments.songs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.zionhuang.music.R
import com.zionhuang.music.extensions.addOnClickListener
import com.zionhuang.music.extensions.requireAppCompatActivity
import com.zionhuang.music.repos.SongRepository
import com.zionhuang.music.ui.adapters.LocalItemPagingAdapter
import com.zionhuang.music.ui.fragments.base.PagingRecyclerViewFragment
import com.zionhuang.music.ui.listeners.SongMenuListener
import com.zionhuang.music.viewmodels.PlaybackViewModel
import com.zionhuang.music.viewmodels.SongsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArtistSongsFragment : PagingRecyclerViewFragment<LocalItemPagingAdapter>() {
    private val args: ArtistSongsFragmentArgs by navArgs()

    private val playbackViewModel by activityViewModels<PlaybackViewModel>()
    private val songsViewModel by activityViewModels<SongsViewModel>()
    override val adapter = LocalItemPagingAdapter().apply {
        songMenuListener = SongMenuListener(this@ArtistSongsFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).addTarget(R.id.fragment_content)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).addTarget(R.id.fragment_content)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addOnClickListener { pos, _ ->
                // TODO
//                if (pos == 0) return@addOnClickListener
//                playbackViewModel.playQueue(requireActivity(),
//                    ListQueue(
//                        title = null,
//                        items = this@ArtistSongsFragment.adapter.snapshot().items.drop(1).map { it.toMediaItem() },
//                        startIndex = pos - 1
//                    )
//                )
            }
        }

        lifecycleScope.launch {
            requireAppCompatActivity().title = SongRepository.getArtistById(args.artistId)!!.name
            songsViewModel.getArtistSongsAsFlow(args.artistId).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}