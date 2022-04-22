package com.example.mediaplayer

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mediaplayer.databinding.ActivityMainBinding
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {
    var isMusicPlaying = false
    lateinit var binding: ActivityMainBinding
    var mediaPlayer: MediaPlayer? = null
    private var fileName: String = ""

    //    private var recordButton: RecordButton? = null
    private var recorder: MediaRecorder? = null
//    private var playButton: PlayButton? = null
//    private var player: MediaPlayer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
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

        binding.buttonStartRecord.setOnClickListener { onRecord(true) }
        binding.buttonStopRecord.setOnClickListener { onRecord(false) }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
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


//    fun record() {
//
//
//        private fun onPlay(start: Boolean) = if (start) {
//            startPlaying()
//        } else {
//            stopPlaying()
//        }
//
//        private fun startPlaying() {
//            player = MediaPlayer().apply {
//                try {
//                    setDataSource(fileName)
//                    prepare()
//                    start()
//                } catch (e: IOException) {
//                    Log.e(LOG_TAG, "prepare() failed")
//                }
//            }
//        }
//
//        private fun stopPlaying() {
//            player?.release()
//            player = null
//        }
//
//
//        internal inner class RecordButton(ctx: Context) : Button(ctx) {
//
//            var mStartRecording = true
//
//            var clicker: OnClickListener = OnClickListener {
//                onRecord(mStartRecording)
//                text = when (mStartRecording) {
//                    true -> "Stop recording"
//                    false -> "Start recording"
//                }
//                mStartRecording = !mStartRecording
//            }
//
//            init {
//                text = "Start recording"
//                setOnClickListener(clicker)
//            }
//        }
//
//        internal inner class PlayButton(ctx: Context) : Button(ctx) {
//            var mStartPlaying = true
//            var clicker: OnClickListener = OnClickListener {
//                onPlay(mStartPlaying)
//                text = when (mStartPlaying) {
//                    true -> "Stop playing"
//                    false -> "Start playing"
//                }
//                mStartPlaying = !mStartPlaying
//            }
//
//            init {
//                text = "Start playing"
//                setOnClickListener(clicker)
//            }
//        }
//
//        override fun onCreate(icicle: Bundle?) {
//            super.onCreate(icicle)
//
//            // Record to the external cache directory for visibility
//            fileName = "${externalCacheDir.absolutePath}/audiorecordtest.3gp"
//
//            ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
//
//            recordButton = RecordButton(this)
//            playButton = PlayButton(this)
//            val ll = LinearLayout(this).apply {
//                addView(
//                    recordButton,
//                    LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0f
//                    )
//                )
//                addView(
//                    playButton,
//                    LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0f
//                    )
//                )
//            }
//            setContentView(ll)
//        }
//
//        override fun onStop() {
//            super.onStop()
//            recorder?.release()
//            recorder = null
//            player?.release()
//            player = null
//        }
//    }
}