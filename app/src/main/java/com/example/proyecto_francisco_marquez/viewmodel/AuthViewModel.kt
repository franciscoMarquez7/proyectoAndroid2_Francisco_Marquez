package com.example.proyecto_francisco_marquez.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider


class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> get() = _userState

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userState.value = auth.currentUser
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    onResult(false, errorMessage)
                }
            }
    }


    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userState.value = auth.currentUser
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    onResult(false, errorMessage)
                }
            }
    }


    fun logout() {
        auth.signOut()
        _userState.value = null
    }

    fun resetPassword(email: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
    fun loginWithGoogle(result: Intent?, onResult: (Boolean, String?) -> Unit) {
        val account = GoogleSignIn.getSignedInAccountFromIntent(result)?.result
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _userState.value = auth.currentUser
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        } else {
            onResult(false, "Google Sign-In failed")
        }
    }

}
