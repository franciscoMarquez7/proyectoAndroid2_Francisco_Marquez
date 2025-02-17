package com.example.proyecto_francisco_marquez.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class InicioConGoogle : ActivityResultContract<Unit, Intent?>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        val webClientId = "AIzaSyBA9mcU-kjYMRxiTDKpgdIMIrH7r-tFulU"

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return intent
    }
}
