package com.makumatthew.vent

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cafe.adriel.androidaudioconverter.AndroidAudioConverter
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudioconverter.model.AudioFormat
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File


class MainActivity : AppCompatActivity() {

    var filePath: String = Environment.getExternalStorageDirectory().toString() + "/recorded_audio.wav"
    var requestCode = 0
    var mp3: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        voicePlayerView.setAudio(filePath);

        Timber.d("converted to mp3 " + filePath)

        record.setOnClickListener {
            checkpermission()
        }

        share.setOnClickListener {
            if (!isHashtagValid(caption.text.toString())){
                Toast.makeText(this, " not valid", Toast.LENGTH_SHORT).show()
                caption.setError("must start with #, e.g #corona");
                Toast.makeText(this, "no numbers.", Toast.LENGTH_SHORT).show()
            } else if (caption.text!!.equals(" ")) {
                caption.setError("must not be empty");
            } else {
                Toast.makeText(this, " valid", Toast.LENGTH_SHORT).show()
            }
        }

//        share.setOnClickListener {
//            if (caption.text!!.equals("") && isHashtagValid(caption.text.toString())){
//                caption.setError("must start with #, e.g #corona");
//            }
//        }

    }

    private fun loadrecorder() {
        AndroidAudioRecorder.with(this)
            // Required
            .setFilePath(filePath)
            .setColor(ContextCompat.getColor(this, R.color.purple_700))
            .setRequestCode(requestCode)

            // Optional
            .setSource(AudioSource.MIC)
            .setChannel(AudioChannel.STEREO)
            .setSampleRate(AudioSampleRate.HZ_48000)
            .setAutoStart(true)
            .setKeepDisplayOn(true)

            // Start recording
            .record();
    }

    private fun checkpermission() {

            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WAKE_LOCK
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                        if (report!!.isAnyPermissionPermanentlyDenied) {
                            // navigate user to app settings
                            DialogUtils.showSettingsDialog(this@MainActivity)
                        } else if (report.areAllPermissionsGranted()) {
                            //permissions granted

                            //disparch
                            GlobalScope.launch(Dispatchers.IO) {
                                //load the audio recorder
                                loadrecorder()

                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            Timber.d("data " + data.toString())
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
                // Now you can convert it
//                val wavFile = File(Environment.getex .getExternalStorageDirectory(), "recorded_audio.wav")
                var filePath: String = Environment.getExternalStorageDirectory().toString() + "recorded_audio.wav"

                val flacFile = File(
                    filePath
                )
                AndroidAudioConverter.with(this) // Your current audio file
                    .setFile(flacFile) // Your desired audio format
                    .setFormat(AudioFormat.MP3) // An callback to know when conversion is finished
                    .setCallback(callback) // Start conversion
                    .convert()

            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Alerter.create(this)
                    .setTitle("Cancelled?")
                    .setText("Oops! you have canceled the recording.")
                    .setDuration(10000)
                    .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
                    .show()
            }
        }
    }

    val callback: IConvertCallback = object : IConvertCallback {
        override fun onSuccess(convertedFile: File?) {
            // send to server(firebase)
            mp3 = convertedFile
            Timber.d("convertedFile " + convertedFile)
        }

        override fun onFailure(error: Exception) {
            // Oops! Something went wrong
            Alerter.create(this@MainActivity)
                .setTitle("Something terribly went wrong.")
                .setText("error:" + error.message)
                .setDuration(10000)
                .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
                .show()
        }
    }

}