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

    private val auth = FirebaseAuth.getInstance()

    private val _userState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> get() = _userState

    init {
        // Configurar listener de estado de autenticación
        auth.addAuthStateListener { firebaseAuth ->
            _userState.value = firebaseAuth.currentUser
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userState.value = auth.currentUser
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
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
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    onResult(false, errorMessage)
                }
            }
    }

    fun logout() {
        auth.signOut()
        _userState.value = null
    }

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isEmpty()) {
            onResult(false, "El email no puede estar vacío")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.localizedMessage ?: "Error al enviar el correo")
                }
            }
    }

    fun loginWithGoogle(result: Intent?, onResult: (Boolean, String?) -> Unit) {
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result).result
            if (account?.idToken != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _userState.value = auth.currentUser
                            onResult(true, null)
                        } else {
                            onResult(false, task.exception?.message ?: "Error al iniciar sesión con Google")
                        }
                    }
            } else {
                onResult(false, "No se obtuvo una cuenta válida")
            }
        } catch (e: Exception) {
            onResult(false, "Error en Google Sign-In: ${e.message}")
        }
    }
}
