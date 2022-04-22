package com.example.mediaplayer

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var isMusicPlaying=false
    lateinit var binding:ActivityMainBinding
     var  mediaPlayer: MediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPlay.setOnClickListener {

            val url = "https://dl.niyazmusic.ir/Golchin/Best/Clip/128/music-clip%201%20%28NiyazMusic%29%28128%29.mp3"// your URL here
             mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepare() // might take long! (for buffering, etc)
                start()
            }
            isMusicPlaying=true
        }

        binding.buttonStop.setOnClickListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }

        binding.buttonPause.setOnClickListener {
            if(isMusicPlaying) {
                mediaPlayer?.pause()
                isMusicPlaying=false
            }
            else {
                mediaPlayer?.start()
                isMusicPlaying=true
            }
        }

    }
}