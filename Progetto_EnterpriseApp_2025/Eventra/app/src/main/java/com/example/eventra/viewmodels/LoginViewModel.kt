package com.example.eventra.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.example.eventra.R
import com.example.eventra.untils.SessionManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val _application = application
    private val server = application.getString(R.string.server)
    private val sessionManager = SessionManager(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    sealed class LoginState {
        object Idle : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email e password sono obbligatori")
            return
        }

        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Inserisci un'email valida")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Idle

            try {
                val success = performLogin(email.trim(), password)
                if (success) {
                    _loginState.value = LoginState.Success
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore durante il login", e)
                _loginState.value = LoginState.Error("Errore di connessione. Riprova più tardi.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(nome: String, cognome: String, email: String, password: String, telefono: String = "") {
        if (nome.isBlank() || cognome.isBlank() || email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Tutti i campi obbligatori devono essere compilati")
            return
        }

        if (nome.length < 2 || nome.length > 12) {
            _loginState.value = LoginState.Error("Il nome deve essere tra 2 e 12 caratteri")
            return
        }

        if (cognome.length < 2 || cognome.length > 12) {
            _loginState.value = LoginState.Error("Il cognome deve essere tra 2 e 12 caratteri")
            return
        }

        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Inserisci un'email valida")
            return
        }

        if (!isValidPassword(password)) {
            _loginState.value = LoginState.Error("La password deve contenere almeno una maiuscola, una minuscola, un numero e un carattere speciale (@\$!%*?&)")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Idle

            try {
                val success = performRegistration(nome.trim(), cognome.trim(), email.trim(), password, telefono.trim())
                if (success) {
                    _loginState.value = LoginState.Success
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore durante la registrazione", e)
                _loginState.value = LoginState.Error("Errore di connessione. Riprova più tardi.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Idle

            try {
                val success = performGoogleLogin(account)
                if (success) {
                    _loginState.value = LoginState.Success
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore durante il login Google", e)
                _loginState.value = LoginState.Error("Errore durante l'autenticazione con Google. Riprova più tardi.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun performLogin(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$server/api/utente/login"
                Log.d("LoginViewModel", "URL login: $url")

                val jsonBody = JSONObject().apply {
                    put("email", email)
                    put("password", password)
                }.toString()

                Log.d("LoginViewModel", "Request body: $jsonBody")

                val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d("LoginViewModel", "Response code: ${response.code}")
                Log.d("LoginViewModel", "Response body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    return@withContext handleSuccessfulAuthResponse(responseBody, "login")
                } else {
                    handleErrorResponse(response.code, responseBody, "login")
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore nella richiesta di login", e)
                _loginState.value = LoginState.Error("Errore di connessione. Verifica la tua rete.")
                false
            }
        }
    }

    private suspend fun performRegistration(
        nome: String,
        cognome: String,
        email: String,
        password: String,
        telefono: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$server/api/utente/register"
                Log.d("LoginViewModel", "URL registrazione: $url")

                val jsonBody = JSONObject().apply {
                    put("nome", nome)
                    put("cognome", cognome)
                    put("email", email)
                    put("password", password)
                    put("numerotelefono", telefono.ifEmpty { "1234567890" })
                }.toString()

                Log.d("LoginViewModel", "Request body: $jsonBody")

                val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d("LoginViewModel", "Response code: ${response.code}")
                Log.d("LoginViewModel", "Response body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    return@withContext handleSuccessfulAuthResponse(responseBody, "registration")
                } else {
                    handleErrorResponse(response.code, responseBody, "registration")
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore nella richiesta di registrazione", e)
                _loginState.value = LoginState.Error("Errore di connessione. Verifica la tua rete.")
                false
            }
        }
    }

    private suspend fun performGoogleLogin(account: GoogleSignInAccount): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$server/api/utente/google-login"
                Log.d("LoginViewModel", "URL Google login: $url")

                val jsonBody = JSONObject().apply {
                    put("googleId", account.id ?: "")
                    put("email", account.email ?: "")
                    put("displayName", account.displayName ?: "")
                    put("givenName", account.givenName ?: "")
                    put("familyName", account.familyName ?: "")
                    put("photoUrl", account.photoUrl?.toString() ?: "")
                    put("idToken", account.idToken ?: "")
                }.toString()

                Log.d("LoginViewModel", "Google request body: $jsonBody")

                val requestBody = jsonBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                Log.d("LoginViewModel", "Google response code: ${response.code}")
                Log.d("LoginViewModel", "Google response body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    return@withContext handleSuccessfulAuthResponse(responseBody, "google-login")
                } else {
                    handleErrorResponse(response.code, responseBody, "google-login")
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore nella richiesta di Google login", e)
                _loginState.value = LoginState.Error("Errore durante l'autenticazione con Google. Verifica la tua rete.")
                false
            }
        }
    }

    private fun handleSuccessfulAuthResponse(responseBody: String, authType: String): Boolean {
        return try {
            val jsonResponse = JSONObject(responseBody)
            val success = jsonResponse.optBoolean("success", false)

            if (success) {
                val dataObject = jsonResponse.optJSONObject("data")
                if (dataObject != null) {
                    val token = dataObject.optString("token", "")
                    val refreshToken = dataObject.optString("refreshToken", "")

                    // Prova diversi campi per l'ID utente
                    val userId = when {
                        dataObject.has("utente") -> dataObject.optLong("utente", -1)
                        dataObject.has("userId") -> dataObject.optLong("userId", -1)
                        dataObject.has("id") -> dataObject.optLong("id", -1)
                        dataObject.has("user") -> {
                            val userObj = dataObject.optJSONObject("user")
                            userObj?.optLong("id", -1) ?: -1
                        }
                        else -> -1L
                    }

                    val userRole = dataObject.optString("role", "USER")

                    Log.d("LoginViewModel", "Auth data - Token: ${token.isNotEmpty()}, UserId: $userId, Role: $userRole")

                    if (token.isNotEmpty() && userId != -1L) {
                        sessionManager.saveUserSession(token, refreshToken, userId, userRole)
                        Log.d("LoginViewModel", "$authType successful, session saved with role: $userRole")
                        true
                    } else {
                        Log.e("LoginViewModel", "Invalid auth data - Token empty: ${token.isEmpty()}, UserId: $userId")
                        _loginState.value = LoginState.Error("Dati di autenticazione non validi dal server")
                        false
                    }
                } else {
                    Log.e("LoginViewModel", "No data object in response")
                    _loginState.value = LoginState.Error("Risposta del server non valida")
                    false
                }
            } else {
                val message = jsonResponse.optString("message", getDefaultErrorMessage(authType))
                Log.e("LoginViewModel", "Auth failed with message: $message")
                _loginState.value = LoginState.Error(message)
                false
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error parsing auth response", e)
            _loginState.value = LoginState.Error("Errore nell'elaborazione della risposta del server")
            false
        }
    }

    private fun handleErrorResponse(code: Int, responseBody: String?, authType: String) {
        Log.e("LoginViewModel", "Error response - Code: $code, Body: $responseBody, Type: $authType")

        try {
            if (responseBody != null) {
                val jsonResponse = JSONObject(responseBody)
                val message = jsonResponse.optString("message", "")

                // Cerca anche in altri campi comuni
                val errorMessage = when {
                    message.isNotEmpty() -> message
                    jsonResponse.has("error") -> jsonResponse.optString("error", "")
                    jsonResponse.has("details") -> jsonResponse.optString("details", "")
                    else -> ""
                }

                if (errorMessage.isNotEmpty()) {
                    val userFriendlyMessage = translateErrorMessage(errorMessage, authType)
                    _loginState.value = LoginState.Error(userFriendlyMessage)
                    return
                }
            }
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error parsing error response", e)
        }

        val errorMessage = when (code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                if (authType == "login") "Email o password non corretti"
                else "Credenziali non valide"
            }
            HttpURLConnection.HTTP_NOT_FOUND -> "Servizio non disponibile"
            HttpURLConnection.HTTP_BAD_REQUEST -> {
                if (authType == "registration") "Alcuni dati inseriti non sono validi"
                else "Richiesta non valida"
            }
            HttpURLConnection.HTTP_CONFLICT -> "Un utente con questa email esiste già"
            HttpURLConnection.HTTP_INTERNAL_ERROR -> "Errore del server. Riprova più tardi"
            HttpURLConnection.HTTP_FORBIDDEN -> "Accesso negato"
            422 -> "Dati non validi. Controlla i campi inseriti"
            429 -> "Troppe richieste. Attendi un momento e riprova"
            else -> "Errore di connessione (Codice: $code). Riprova più tardi"
        }

        _loginState.value = LoginState.Error(errorMessage)
    }

    private fun translateErrorMessage(serverMessage: String, authType: String): String {
        val lowerMessage = serverMessage.lowercase()

        return when {
            lowerMessage.contains("foreign key") || lowerMessage.contains("constraint") ->
                "Errore interno del server. Riprova più tardi"
            lowerMessage.contains("duplicate") || lowerMessage.contains("unique") ->
                "Un utente con questa email esiste già"
            lowerMessage.contains("invalid") && lowerMessage.contains("email") ->
                "Formato email non valido"
            lowerMessage.contains("password") && lowerMessage.contains("weak") ->
                "La password non rispetta i criteri di sicurezza"
            lowerMessage.contains("user not found") || lowerMessage.contains("utente non trovato") ->
                "Account non trovato con questa email"
            lowerMessage.contains("wrong password") || lowerMessage.contains("password incorrect") ->
                "Password non corretta"
            lowerMessage.contains("account disabled") || lowerMessage.contains("account suspended") ->
                "Account disabilitato. Contatta il supporto"
            lowerMessage.contains("google") && lowerMessage.contains("failed") ->
                "Errore durante l'autenticazione con Google"
            lowerMessage.contains("token") && lowerMessage.contains("invalid") ->
                "Sessione non valida. Riprova l'accesso"

            authType == "registration" -> "Errore durante la registrazione. Verifica i dati inseriti"
            authType == "login" -> "Errore durante l'accesso. Verifica email e password"
            authType == "google-login" -> "Errore durante l'accesso con Google"

            else -> serverMessage.take(100) // Limita la lunghezza del messaggio
        }
    }

    private fun getDefaultErrorMessage(authType: String): String {
        return when (authType) {
            "registration" -> "Errore durante la registrazione"
            "google-login" -> "Errore durante l'accesso con Google"
            else -> "Credenziali non valide"
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$"
        return password.matches(pattern.toRegex())
    }

    fun isUserLoggedIn(): Boolean = sessionManager.isLoggedIn()

    fun clearError() {
        _loginState.value = LoginState.Idle
    }

    fun logout() {
        sessionManager.clearSession()
        _loginState.value = LoginState.Idle
    }

    fun refreshToken() {
        val refreshToken = sessionManager.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            logout()
            return
        }

        viewModelScope.launch {
            try {
                val success = performTokenRefresh(refreshToken)
                if (!success) {
                    logout()
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore refresh token", e)
                logout()
            }
        }
    }

    private suspend fun performTokenRefresh(refreshToken: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$server/api/utente/refresh?refreshtoken=$refreshToken"

                val request = Request.Builder()
                    .url(url)
                    .post("".toRequestBody())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val newToken = jsonResponse.optString("token", "")
                    val newRefreshToken = jsonResponse.optString("refreshToken", refreshToken)
                    val userId = sessionManager.getUserId()
                    val userRole = sessionManager.getUserRole()

                    if (newToken.isNotEmpty() && userId != -1L) {
                        sessionManager.saveUserSession(newToken, newRefreshToken, userId, userRole)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Errore refresh token", e)
                false
            }
        }
    }
}