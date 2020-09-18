package com.makumatthew.vent

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.tapadoo.alerter.Alerter
import ke.kazinow.app.VentApplication
import java.lang.Boolean


class IntroActivity : AppIntro() {

    val prefManager = VentApplication.instance!!.prefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiStuff()
    }

    var prevStarted = "prevStarted"
    override fun onResume() {
        super.onResume()
        val sharedpreferences =
            getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            val editor = sharedpreferences.edit()
            editor.putBoolean(prevStarted, Boolean.TRUE)
            editor.apply()
        } else {
            finish()
        }
    }

    private fun uiStuff() {

        setTransformer(AppIntroPageTransformerType.Zoom)

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
                title = "Welcome...",
                description = "Your voice matters, say whatever you want.",
                imageDrawable = R.drawable.head,
                titleColor = Color.WHITE,
                backgroundColor = resources.getColor(R.color.purple_700)

            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                title = "Record your message.",
                description = "Press the play button to start recording",
                imageDrawable = R.drawable.head,
                titleColor = Color.WHITE,
                backgroundColor = resources.getColor(R.color.purple_700)
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                title = "Share your message.",
                description = "Let other people also hear your opinions, they matter!!!",
                imageDrawable = R.drawable.head,
                titleColor = Color.WHITE,
                backgroundColor = resources.getColor(R.color.purple_700)
            )
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        prefManager.isFirstTimeLaunch = false
        goToActivity()
    }

    private fun goToActivity() {
        val myIntent = Intent(this, LoginActivity::class.java)
        overridePendingTransition(0, 0)
        this.startActivity(myIntent)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        prefManager.isFirstTimeLaunch = false
        goToActivity()
    }

    override fun onUserDeniedPermission(permissionName: String) {
        // User pressed "Deny" on the permission dialog
        // User pressed "Deny" + "Don't ask again" on the permission dialog
        Alerter.create(this)
            .setTitle("Permissions required")
            .setText("For best experience, you have to accept permissions.")
            .setDuration(10000)
            .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
            .addButton("Okay", R.style.AlertButton, View.OnClickListener {
                Toast.makeText(this, "Okay Clicked", Toast.LENGTH_LONG).show()
            })
            .show()
    }
    override fun onUserDisabledPermission(permissionName: String) {
        // User pressed "Deny" + "Don't ask again" on the permission dialog
        Alerter.create(this)
            .setTitle("Permissions required")
            .setText("For best experience, you have to accept permissions. Please restart this app.")
            .setDuration(10000)
            .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
            .addButton("Okay", R.style.AlertButton, View.OnClickListener {
                Toast.makeText(this, "Okay Clicked", Toast.LENGTH_LONG).show()
            })
            .show()
    }
}