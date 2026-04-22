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
import com.example.eventra.viewmodels.data.PagamentoData
import com.example.eventra.viewmodels.data.PagamentoRequest
import java.io.IOException
import java.math.BigDecimal
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class PagamentoViewModel(application: Application) : AndroidViewModel(application) {

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

    private val _pagamenti = MutableStateFlow<List<PagamentoData>>(emptyList())
    val pagamenti: StateFlow<List<PagamentoData>> = _pagamenti.asStateFlow()

    private val _pagamentoCreated = MutableStateFlow<PagamentoData?>(null)
    val pagamentoCreated: StateFlow<PagamentoData?> = _pagamentoCreated.asStateFlow()

    private val _error = MutableStateFlow<ErrorData?>(null)
    val error: StateFlow<ErrorData?> = _error.asStateFlow()

    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()

    fun getPagamentiByUtente(utenteId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("PagamentoViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante")
                    return@launch
                }

                val url = "$server/api/pagamenti/$utenteId"
                Log.d("PagamentoViewModel", "Getting pagamenti by utente: $url")

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
                Log.d("PagamentoViewModel", "Response pagamenti: $responseJson")

                val pagamentiList = gson.fromJson(responseJson, Array<PagamentoData>::class.java).toList()
                _pagamenti.value = pagamentiList

            } catch (e: IOException) {
                Log.e("PagamentoViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
            } catch (e: Exception) {
                Log.e("PagamentoViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createPagamento(
        ordineId: Long,
        pagamentoRequest: PagamentoRequest,
        onSuccess: ((PagamentoData) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true
            _paymentSuccess.value = false

            try {
                val token = sessionManager.getJwtToken()
                if (token.isNullOrEmpty()) {
                    Log.e("PagamentoViewModel", "Token JWT non disponibile")
                    _error.value = ErrorData(401, "Token JWT mancante o non valido")
                    return@launch
                }

                val isValidCard = validateCreditCard(pagamentoRequest)
                if (!isValidCard) {
                    _error.value = ErrorData(400, "Dati della carta non validi")
                    return@launch
                }

                val url = "$server/api/pagamenti/createPagamento/$ordineId"
                Log.d("PagamentoViewModel", "Creating pagamento at $url")

                val pagamentoDTO = mapOf(
                    "nomeTitolare" to pagamentoRequest.nomeTitolare,
                    "cognomeTitolare" to pagamentoRequest.cognomeTitolare,
                    "numeroCarta" to pagamentoRequest.numeroCarta,
                    "scadenza" to pagamentoRequest.scadenza,
                    "cvv" to pagamentoRequest.cvv,
                    "importo" to BigDecimal(pagamentoRequest.importo)
                )

                val requestJson = gson.toJson(pagamentoDTO)
                Log.d("PagamentoViewModel", "Request: $requestJson")

                val requestBody = requestJson.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                Log.d("PagamentoViewModel", "Response code: ${response.code}")

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
                            Log.e("PagamentoViewModel", "Error response: $errorBody")
                            _error.value = ErrorData(response.code, errorBody)
                        }
                    }
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("PagamentoViewModel", "Response: $responseJson")

                val pagamentoCreato = gson.fromJson(responseJson, PagamentoData::class.java)
                _pagamentoCreated.value = pagamentoCreato
                _paymentSuccess.value = true

                // Simulazione processamento (2 secondi)
                kotlinx.coroutines.delay(2000)

                onSuccess?.invoke(pagamentoCreato)

            } catch (e: IOException) {
                Log.e("PagamentoViewModel", "Network error", e)
                _error.value = ErrorData(0, "Errore di connessione")
            } catch (e: Exception) {
                Log.e("PagamentoViewModel", "Unexpected error", e)
                _error.value = ErrorData(0, "Errore imprevisto: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validateCreditCard(pagamentoRequest: PagamentoRequest): Boolean {
        return pagamentoRequest.nomeTitolare.isNotBlank() &&
                pagamentoRequest.cognomeTitolare.isNotBlank() &&
                pagamentoRequest.numeroCarta.length >= 13 &&
                pagamentoRequest.cvv.length >= 3 &&
                pagamentoRequest.scadenza.matches(Regex("\\d{2}/\\d{2}"))
    }

    fun resetPaymentState() {
        Log.d("PagamentoViewModel", "Resetting payment state")
        _paymentSuccess.value = false
        _pagamentoCreated.value = null
        _error.value = null
        _isLoading.value = false
    }

    fun resetAll() {
        Log.d("PagamentoViewModel", "Complete reset of PagamentoViewModel")
        _paymentSuccess.value = false
        _pagamentoCreated.value = null
        _error.value = null
        _isLoading.value = false
        _pagamenti.value = emptyList()
    }
}
