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
import com.example.eventra.viewmodels.data.*
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class BigliettoViewModel(application: Application) : AndroidViewModel(application) {

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

    private val _biglietti = MutableStateFlow<List<BigliettoInfoDTO>>(emptyList())
    val biglietti: StateFlow<List<BigliettoInfoDTO>> = _biglietti.asStateFlow()

    private val _bigliettoCreated = MutableStateFlow<BigliettoInfoDTO?>(null)
    val bigliettoCreated: StateFlow<BigliettoInfoDTO?> = _bigliettoCreated.asStateFlow()

    private val _bigliettiCount = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val bigliettiCount: StateFlow<Map<Long, Long>> = _bigliettiCount.asStateFlow()

    private val _error = MutableStateFlow<ErrorData?>(null)
    val error: StateFlow<ErrorData?> = _error.asStateFlow()

    fun createBiglietto(
        bigliettoData: BigliettoData,
        onSuccess: ((BigliettoInfoDTO) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("BigliettoViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante o non valido")
                    onError?.invoke("Token non disponibile")
                    return@launch
                }

                val url = "$server/api/biglietto/create"
                Log.d("BigliettoViewModel", "Creating biglietto at $url")

                // Converto in DTO per il backend
                val createRequest = BigliettoCreateRequest(
                    nomeSpettatore = bigliettoData.nomeSpettatore,
                    cognomeSpettatore = bigliettoData.cognomeSpettatore,
                    emailSpettatore = bigliettoData.emailSpettatore,
                    eventoId = bigliettoData.eventoId,
                    tipoPostoId = bigliettoData.tipoPostoId,
                    pagamentoId = bigliettoData.pagamentoId ?: 0L
                )

                val requestJson = gson.toJson(createRequest)
                Log.d("BigliettoViewModel", "Request: $requestJson")

                val requestBody = requestJson.toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                Log.d("BigliettoViewModel", "Response code: ${response.code}")

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
                    onError?.invoke("Errore nella creazione del biglietto")
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("BigliettoViewModel", "Response: $responseJson")

                val bigliettoCreato = gson.fromJson(responseJson, BigliettoInfoDTO::class.java)
                _bigliettoCreated.value = bigliettoCreato
                onSuccess?.invoke(bigliettoCreato)

            } catch (e: IOException) {
                Log.e("BigliettoViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
                onError?.invoke("Errore di connessione")
            } catch (e: Exception) {
                Log.e("BigliettoViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto")
                onError?.invoke("Errore imprevisto")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getBigliettiByUtente(utenteId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("BigliettoViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante")
                    return@launch
                }

                val url = "$server/api/biglietto/utente/$utenteId"
                Log.d("BigliettoViewModel", "Getting biglietti by utente: $url")

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
                Log.d("BigliettoViewModel", "Response biglietti: $responseJson")

                val bigliettiList = gson.fromJson(responseJson, Array<BigliettoInfoDTO>::class.java).toList()
                _biglietti.value = bigliettiList

            } catch (e: IOException) {
                Log.e("BigliettoViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
            } catch (e: Exception) {
                Log.e("BigliettoViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        Log.d("BigliettoViewModel", "Resetting biglietto state")
        _isLoading.value = false
        _error.value = null
    }

    fun resetAll() {
        Log.d("BigliettoViewModel", "Complete reset of BigliettoViewModel")
        _isLoading.value = false
        _error.value = null
    }

    fun countBigliettiByEvento(eventoId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("BigliettoViewModel", "Token JWT non disponibile per count")
                    return@launch
                }

                val url = "$server/api/biglietto/count/$eventoId"
                Log.d("BigliettoViewModel", "Counting biglietti for evento: $url")

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val count = response.body?.string()?.toLongOrNull() ?: 0L
                    Log.d("BigliettoViewModel", "Count for evento $eventoId: $count")

                    // Aggiorna il map con il conteggio per questo evento
                    val currentMap = _bigliettiCount.value.toMutableMap()
                    currentMap[eventoId] = count
                    _bigliettiCount.value = currentMap
                } else {
                    Log.e("BigliettoViewModel", "Error counting biglietti: ${response.code}")
                }

            } catch (e: IOException) {
                Log.e("BigliettoViewModel", "Network error in count", e)
            } catch (e: Exception) {
                Log.e("BigliettoViewModel", "Unexpected error in count", e)
            }
        }
    }

}
