package com.makumatthew.vent.ui.fragment.add

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import cafe.adriel.androidaudioconverter.callback.IConvertCallback
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder
import cafe.adriel.androidaudiorecorder.model.AudioChannel
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate
import cafe.adriel.androidaudiorecorder.model.AudioSource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.makumatthew.vent.R
import com.makumatthew.vent.utils.DialogUtils
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.bottomsheet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class AddFragment : BottomSheetDialogFragment() {

  private lateinit var addViewModel: AddViewModel
  var filePath: String = Environment.getExternalStorageDirectory().toString() + "/recorded_audio.wav"
  var requestCode = 0
  var mp3: File? = null
  
  //firebase
  private var mStorageRef: StorageReference? = null
  // Create a reference to 'images/mountains.jpg'

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
     super.onCreateView(inflater, container, savedInstanceState)
    val view: View = inflater.inflate(R.layout.bottomsheet, container, false)
    addViewModel =
      ViewModelProvider(this).get(AddViewModel::class.java)

    mStorageRef = FirebaseStorage.getInstance().reference;

    return view
  }


  private fun loadrecorder() {
    AndroidAudioRecorder.with(requireActivity())
      // Required
      .setFilePath(filePath)
      .setColor(ContextCompat.getColor(requireContext(), R.color.purple_700))
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

    Dexter.withContext(requireContext())
      .withPermissions(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.WAKE_LOCK
      )
      .withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

          if (report!!.isAnyPermissionPermanentlyDenied) {
            // navigate user to app settings
            DialogUtils.showSettingsDialog(requireContext())
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
      if (resultCode == AppCompatActivity.RESULT_OK) {
        // Great! User has recorded and saved the audio file
        // Now you can convert it
//                val wavFile = File(Environment.getex .getExternalStorageDirectory(), "recorded_audio.wav")
        var filePath: String = Environment.getExternalStorageDirectory().toString() + "recorded_audio.wav"

        val flacFile = File(
          filePath
        )

//
//        AndroidAudioConverter.with(requireContext()) // Your current audio file
//          .setFile(flacFile) // Your desired audio format
//          .setFormat(AudioFormat.MP3) // An callback to know when conversion is finished
//          .setCallback(callback) // Start conversion
//          .convert()

      } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
        // Oops! User has canceled the recording
        Alerter.create(requireActivity())
          .setTitle("Cancelled?")
          .setText("Oops! you have canceled the recording.")
          .setDuration(10000)
          .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
          .show()
      }
    }
  }

  private fun sendToFirebase(audio: File) {
    Timber.d("sending to firebase...")

    Timber.d("converted  " + audio)
    val file = Uri.fromFile(audio)
    var uploadTask = mStorageRef?.putFile(file)
    uploadTask?.addOnFailureListener {
      // Handle unsuccessful uploads
    }?.addOnSuccessListener { taskSnapshot ->
      // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
      // ...
    }


//    val file = Uri.fromFile(File(filePath))
//    val mStorageRef: StorageReference = (mStorageRef?.child(filePath) ?: mStorageRef?.putFile(file)
//        ?.addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
//          val downloadUrl: String = taskSnapshot.metadata.toString()
//          Toast.makeText(requireContext(), "url " + downloadUrl, Toast.LENGTH_SHORT).show()
//        }
//        ?.addOnFailureListener {
//          // Handle unsuccessful uploads
//          // ...
//        }) as StorageReference
  }

  val callback: IConvertCallback = object : IConvertCallback {
    override fun onSuccess(convertedFile: File?) {
      // send to server(firebase)
      mp3 = convertedFile
      Timber.d("convertedFile " + convertedFile)
    }

    override fun onFailure(error: Exception) {
      // Oops! Something went wrong
//            Alerter.create(this@MainActivity)
//                .setTitle("Something terribly went wrong.")
//                .setText("error:" + error.message)
//                .setDuration(10000)
//                .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
//                .show()
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      AddFragment().apply {
        arguments = Bundle().apply {
        }
      }
  }
}
