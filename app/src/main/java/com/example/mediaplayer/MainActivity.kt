package com.example.mediaplayer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mediaplayer.databinding.ActivityMainBinding
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {
    var isMusicPlaying = false
    lateinit var binding: ActivityMainBinding
    var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()


    }

    private fun initViews() {
        binding.buttonPlay.setOnClickListener {

            val url = getString(R.string.music1)// your URL here
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
            isMusicPlaying = true
        }

        binding.buttonStop.setOnClickListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }

        binding.buttonPause.setOnClickListener {
            if (isMusicPlaying) {
                mediaPlayer?.pause()
                isMusicPlaying = false
            } else {
                mediaPlayer?.start()
                isMusicPlaying = true
            }
        }

        binding.buttonStartRecord.setOnClickListener {

        }
        binding.buttonStopRecord.setOnClickListener {

        }
    }


}