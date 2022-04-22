package com.example.mediaplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mediaplayer.databinding.ActivityMainBinding
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {
    var isMusicPlaying = false
    var mStartRecording = true
    private var player: MediaPlayer? = null
    lateinit var binding: ActivityMainBinding
    var mediaPlayer: MediaPlayer? = null
    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    var mStartPlaying = true
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    "you granted this permission",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "you denied this permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        requestPermissions()
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
            startRecord()
        }
        binding.buttonPlayRecorded.setOnClickListener {
            startPlay()
        }
    }







    private fun startPlay() {
        onPlay(mStartPlaying)
        binding.buttonPlayRecorded.text = when (mStartPlaying) {
            true -> "Stop"
            false -> "play"
        }
        mStartPlaying = !mStartPlaying
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }





    private fun startRecord() {
        onRecord(mStartRecording)
        binding.buttonStartRecord.text = when (mStartRecording) {
            true -> "Stop recording"
            false -> "record"
        }
        mStartRecording = !mStartRecording
    }


    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }


    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                //if user already granted the permission
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Toast.makeText(
                        this,
                        "you have already granted this permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //if user already denied the permission once
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) -> {
                }
                else -> {
                    requestPermissionLauncher.launch(
                        Manifest.permission.RECORD_AUDIO
                    )
                }
            }
        }
    }

    //    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
//            grantResults[0] == PackageManager.PERMISSION_GRANTED
//        } else {
//            false
//        }
//        if (!permissionToRecordAccepted) finish()
//    }


}