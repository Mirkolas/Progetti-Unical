package com.example.eventra.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.eventra.R
import com.example.eventra.untils.SessionManager
import com.example.eventra.viewmodels.data.ErrorData
import com.example.eventra.viewmodels.data.OrdineData
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class OrdineViewModel(application: Application) : AndroidViewModel(application) {

    private val _application = application
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    private val server = application.getString(R.string.server)
    private val sessionManager = SessionManager(application)

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _ordineCreated = MutableStateFlow<OrdineData?>(null)
    val ordineCreated: StateFlow<OrdineData?> = _ordineCreated.asStateFlow()

    private val _ordini = MutableStateFlow<List<OrdineData>>(emptyList())
    val ordini: StateFlow<List<OrdineData>> = _ordini.asStateFlow()

    private val _error = MutableStateFlow<ErrorData?>(null)
    val error: StateFlow<ErrorData?> = _error.asStateFlow()

    fun aggiungiOrdine(
        ordineData: OrdineData,
        idProprietario: Long,
        onSuccess: ((OrdineData) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("OrdineViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante o non valido")
                    return@launch
                }

                val url = "$server/api/ordine/aggiungi/$idProprietario"
                Log.d("OrdineViewModel", "Creating ordine at $url")

                val ordineRequest = mapOf(
                    "emailProprietario" to ordineData.emailProprietario,
                    "prezzoTotale" to ordineData.prezzoTotale
                )

                val requestJson = gson.toJson(ordineRequest)
                Log.d("OrdineViewModel", "Request: $requestJson")

                val requestBody = requestJson.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                Log.d("OrdineViewModel", "Response code: ${response.code}")

                if (!response.isSuccessful) {
                    when (response.code) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            sessionManager.clearSession()
                            _error.value = ErrorData(401, "Sessione scaduta")
                        }
                        HttpURLConnection.HTTP_UNSUPPORTED_TYPE -> {
                            _error.value = ErrorData(415, "Formato non supportato")
                        }
                        else -> {
                            val errorBody = response.body?.string() ?: "Errore sconosciuto"
                            Log.e("OrdineViewModel", "Error response: $errorBody")
                            _error.value = ErrorData(response.code, errorBody)
                        }
                    }
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("OrdineViewModel", "Response: $responseJson")

                val ordineCreato = gson.fromJson(responseJson, OrdineData::class.java)
                _ordineCreated.value = ordineCreato
                onSuccess?.invoke(ordineCreato)

            } catch (e: IOException) {
                Log.e("OrdineViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
            } catch (e: Exception) {
                Log.e("OrdineViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getOrdiniByUtente(utenteId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("OrdineViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante")
                    return@launch
                }

                val url = "$server/api/ordine/utente/$utenteId"
                Log.d("OrdineViewModel", "Getting ordini by utente: $url")

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    when (response.code) {
                        HttpURLConnection.HTTP_UNAUTHORIZED -> {
                            sessionManager.clearSession()
                            _error.value = ErrorData(401, "Sessione scaduta")
                        }
                        else -> {
                            val errorBody = response.body?.string() ?: "Errore sconosciuto"
                            _error.value = ErrorData(response.code, errorBody)
                        }
                    }
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("OrdineViewModel", "Response ordini: $responseJson")

                val ordiniList = gson.fromJson(responseJson, Array<OrdineData>::class.java).toList()
                _ordini.value = ordiniList

            } catch (e: IOException) {
                Log.e("OrdineViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
            } catch (e: Exception) {
                Log.e("OrdineViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetOrdineState() {
        Log.d("OrdineViewModel", "Resetting order state")
        _ordineCreated.value = null
        _ordini.value = emptyList()
        _error.value = null
        _isLoading.value = false
    }

    fun resetAll() {
        Log.d("OrdineViewModel", "Complete reset of OrdineViewModel")
        _ordineCreated.value = null
        _ordini.value = emptyList()
        _error.value = null
        _isLoading.value = false
    }
}
