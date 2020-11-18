package com.makumatthew.vent.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.makumatthew.vent.R
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var signInOptions: GoogleSignInOptions? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//       val loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener {
            googleLogin()
        }

        // [START config_signin]
        // Configure Google Sign In
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    private fun googleLogin() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Toast.makeText(this, "Signing in", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener { task1 ->
                //handle successfull sign in
                if (task1.isSuccessful) {
                    val account = task1.result
                    Toast.makeText(this, "Welcome ${account!!.email!!} !", Toast.LENGTH_SHORT).show()

                    //save user to app
                    GoogleManager(account)

                    firebaseAuthWithGoogle(account)

                } else {
                    Toast.makeText(this, "Welcome ${task1.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "signInWithCredential: success", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    //check new account
                    updateUI(user)
                } else {
                    updateUI(null)
                    // If sign in fails, display a message to the user.
                    Alerter.create(this)
                        .setTitle("user is null")
                        .setText(task.exception!!.message.toString())
                        .setDuration(10000)
                        .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
                        .show()

                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user!=null){
            goToMainActivity()
        } else {

        }

        //  showLoginDialog()
    }

    private fun showLoginDialog() {
        Alerter.create(this)
            .setTitle("Login")
            .setText("Login with google.")
            .setDuration(10000)
            .setBackgroundColorRes(R.color.purple_700) // or setBackgroundColorInt(Color.CYAN)
            .addButton("Google", R.style.AlertButton, View.OnClickListener {

            })
            .show()
    }


    private fun GoogleManager(account: GoogleSignInAccount ) {
        val id = account.id.toString()
        val firstName = account.displayName.toString()
        val lastName = account.displayName.toString()
        val email = account.email
        val photoUrl = account.photoUrl.toString()
        Log.d("user data %s%s", firstName + lastName + email + photoUrl)
    }

    private fun goToMainActivity() {
        val myIntent = Intent(this, DeleteActivity::class.java)
        this.startActivity(myIntent)
    }

}