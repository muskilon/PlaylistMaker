package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMedialibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class MedialibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMedialibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()


        binding.backArrow.setOnClickListener {
            this.finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}