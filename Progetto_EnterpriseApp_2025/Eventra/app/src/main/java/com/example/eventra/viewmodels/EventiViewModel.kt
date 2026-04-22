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
import okhttp3.OkHttpClient
import okhttp3.Request
import com.example.eventra.R
import com.example.eventra.untils.SessionManager
import com.example.eventra.viewmodels.data.ErrorData
import com.example.eventra.viewmodels.data.EventoData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.EOFException
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

data class EventoUpdateRequest(
    val id: Long,
    val nome: String,
    val descrizione: String,
    val dataOraEvento: String,
    val dataOraAperturaCancelli: String,
    val postiDisponibili: Int,

)

data class EventoCreateRequest(
    val nome: String,
    val descrizione: String,
    val categoriaId: Long,
    val immagine: String?,
    val dataOraEvento: String,
    val dataOraAperturaCancelli: String,
    val postiDisponibili: Int,
    val luogo: String,
    val organizzatoreId: Long,
    val strutturaId: Long,
    val biglietti: List<Any> = emptyList(),
    val tipiPosto: List<Any> = emptyList()
)

class EventiViewModel(application: Application) : AndroidViewModel(application) {

    private val _application = application
    private val gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .create()
    private val server = application.getString(R.string.server)
    private val backendUrl = URL("$server/api/evento")



    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _eventi = MutableStateFlow<List<EventoData>?>(emptyList())
    val eventi: StateFlow<List<EventoData>?> = _eventi.asStateFlow()

    private val _eventiByCategoria = MutableStateFlow<List<EventoData>?>(emptyList())
    val eventiByCategoria: StateFlow<List<EventoData>?> = _eventiByCategoria.asStateFlow()

    private val _eventiByOrganizzatore = MutableStateFlow<List<EventoData>?>(emptyList())
    val eventiByOrganizzatore: StateFlow<List<EventoData>?> = _eventiByOrganizzatore.asStateFlow()

    private val _eventoDetail = MutableStateFlow<EventoData?>(null)
    val eventoDetail: StateFlow<EventoData?> = _eventoDetail.asStateFlow()

    private val _error = MutableStateFlow<ErrorData?>(null)
    val error: StateFlow<ErrorData?> = _error.asStateFlow()

    private val _deleteSuccess = MutableStateFlow<Boolean?>(null)
    val deleteSuccess: StateFlow<Boolean?> = _deleteSuccess.asStateFlow()

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess.asStateFlow()

    private val _createSuccess = MutableStateFlow<Boolean?>(null)
    val createSuccess: StateFlow<Boolean?> = _createSuccess.asStateFlow()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val sessionManager = SessionManager(application)

    private fun getAuthenticatedRequest(urlString: String): Request.Builder {
        val token = sessionManager.getJwtToken()

        if (token.isNullOrEmpty()) {
            Log.e("WishlistViewModel", "Token JWT non disponibile")
            throw IllegalStateException("Token JWT non disponibile")
        }

        return Request.Builder()
            .url(urlString)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
    }

    fun getAllEventi() = getEventiNoAuth("", _eventi, "allEventi")

    fun getEventiByCategoria(categoriaId: Long) = getEventiNoAuth("/categoria/$categoriaId", _eventiByCategoria, "categoria")

    fun getEventiByOrganizzatore(organizzatoreId: Long) = getEventi("/organizzatore/$organizzatoreId", _eventiByOrganizzatore, "organizzatore")

    fun createEvento(
        nome: String,
        descrizione: String,
        categoriaId: Long,
        immagine: String?,
        dataOraEvento: String,
        dataOraAperturaCancelli: String,
        postiDisponibili: Int,
        luogo: String,
        organizzatoreId: Long,
        strutturaId: Long,
        onSuccess: ((EventoData) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true
            _createSuccess.value = null

            val urlString = "$backendUrl/create"
            Log.d("EventiViewModel", "Creating evento: $urlString")

            val createRequest = EventoCreateRequest(
                nome = nome,
                descrizione = descrizione,
                categoriaId = categoriaId,
                immagine = immagine,
                dataOraEvento = dataOraEvento,
                dataOraAperturaCancelli = dataOraAperturaCancelli,
                postiDisponibili = postiDisponibili,
                luogo = luogo,
                organizzatoreId = organizzatoreId,
                strutturaId = strutturaId,
                biglietti = emptyList(),
                tipiPosto = emptyList()
            )

            val requestJson = gson.toJson(createRequest)
            Log.d("EventiViewModel", "Create request JSON: $requestJson")

            val requestBody = requestJson.toRequestBody("application/json".toMediaType())
            val request = getAuthenticatedRequest(urlString)
                .post(requestBody)
                .build()

            try {
                val response = client.newCall(request).execute()


                val responseBody = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Create response code: ${response.code}")
                Log.d("EventiViewModel", "Create response headers: ${response.headers}")
                Log.d("EventiViewModel", "Create response body: $responseBody")

                if (response.isSuccessful) {
                    val createdEvento = gson.fromJson(responseBody, EventoData::class.java)

                    val currentEvents = _eventiByOrganizzatore.value?.toMutableList() ?: mutableListOf()
                    currentEvents.add(createdEvento)
                    _eventiByOrganizzatore.value = currentEvents

                    _createSuccess.value = true
                    Log.d("EventiViewModel", "Evento creato con successo: ${createdEvento.id}")
                    onSuccess?.invoke(createdEvento)
                } else {
                    val errorMessage = "Errore ${response.code}: $responseBody"
                    _error.value = ErrorData(response.code, errorMessage)
                    Log.e("EventiViewModel", "Error creating evento: $errorMessage")
                    onError?.invoke(errorMessage)
                }

            } catch (e: Exception) {
                val errorMsg = "Errore durante la creazione: ${e.message}"
                _error.value = ErrorData(0, errorMsg)
                Log.e("EventiViewModel", "Error creating evento: ${e.message}")
                onError?.invoke(errorMsg)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun resetCreateState() {
        _createSuccess.value = null
        _error.value = null
    }

    fun updateEvento(
        eventoOriginale: EventoData,
        nome: String,
        descrizione: String,
        postiDisponibili: Int,
        dataOraEvento: String,
        dataOraAperturaCancelli: String,
        onSuccess: ((EventoData) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true
            _updateSuccess.value = null

            val urlString = "$backendUrl/update/${eventoOriginale.id}"
            Log.d("EventiViewModel", "Updating evento: $urlString")

            val updateRequest = EventoUpdateRequest(
                id = eventoOriginale.id,
                nome = nome,
                descrizione = descrizione,
                dataOraEvento = dataOraEvento,
                dataOraAperturaCancelli = dataOraAperturaCancelli,
                postiDisponibili = postiDisponibili,

            )

            val requestJson = gson.toJson(updateRequest)
            Log.d("EventiViewModel", "Update request: $requestJson")



            val requestBody = requestJson.toRequestBody("application/json".toMediaType())
            val request = getAuthenticatedRequest(urlString)
                .put(requestBody)
                .build()
            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseJson = response.body?.string() ?: ""
                    Log.d("EventiViewModel", "Update response: $responseJson")

                    val updatedEvento = gson.fromJson(responseJson, EventoData::class.java)

                    val currentEvents = _eventiByOrganizzatore.value?.toMutableList()
                    currentEvents?.let { list ->
                        val index = list.indexOfFirst { it.id == eventoOriginale.id }
                        if (index != -1) {
                            list[index] = updatedEvento
                            _eventiByOrganizzatore.value = list
                        }
                    }

                    _updateSuccess.value = true
                    Log.d("EventiViewModel", "Evento ${eventoOriginale.id} aggiornato con successo")
                    onSuccess?.invoke(updatedEvento)
                } else {
                    val errorMessage = when (response.code) {
                        404 -> "Evento non trovato"
                        400 -> "Dati non validi"
                        415 -> "Formato richiesta non supportato"
                        else -> "Errore durante l'aggiornamento: ${response.code}"
                    }
                    _error.value = ErrorData(response.code, errorMessage)
                    Log.e("EventiViewModel", "Error updating evento: ${response.code}")
                    onError?.invoke(errorMessage)
                }

            } catch (e: IOException) {
                val errorMsg = "Errore di connessione durante l'aggiornamento"
                _error.value = ErrorData(0, errorMsg)
                Log.e("EventiViewModel", "Network error updating evento: ${e.message}")
                onError?.invoke(errorMsg)

            } catch (e: Exception) {
                val errorMsg = "Errore imprevisto durante l'aggiornamento"
                _error.value = ErrorData(0, errorMsg)
                Log.e("EventiViewModel", "Unexpected error updating evento: ${e.message}")
                onError?.invoke(errorMsg)

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteEvento(
        eventoId: Long,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true
            _deleteSuccess.value = null

            val urlString = "$backendUrl/delete/$eventoId"
            Log.d("EventiViewModel", "Deleting evento: $urlString")

            val request = getAuthenticatedRequest(urlString).delete().build()


            try {
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("EventiViewModel", "Evento $eventoId eliminato con successo")
                    _deleteSuccess.value = true

                    // Rimuovi l'evento dalla lista locale
                    val currentEvents = _eventiByOrganizzatore.value?.toMutableList()
                    currentEvents?.removeAll { it.id == eventoId }
                    _eventiByOrganizzatore.value = currentEvents ?: emptyList()

                    onSuccess?.invoke()
                } else {
                    val errorMessage = when (response.code) {
                        404 -> "Evento non trovato"
                        else -> "Errore durante l'eliminazione: ${response.code}"
                    }
                    _error.value = ErrorData(response.code, errorMessage)
                    Log.e("EventiViewModel", "Error deleting evento: ${response.code}")
                    onError?.invoke(errorMessage)
                }

            } catch (e: IOException) {
                val errorMsg = "Errore di connessione durante l'eliminazione"
                _error.value = ErrorData(0, errorMsg)
                Log.e("EventiViewModel", "Network error deleting evento: ${e.message}")
                onError?.invoke(errorMsg)

            } catch (e: Exception) {
                val errorMsg = "Errore imprevisto durante l'eliminazione"
                _error.value = ErrorData(0, errorMsg)
                Log.e("EventiViewModel", "Unexpected error deleting evento: ${e.message}")
                onError?.invoke(errorMsg)

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetDeleteState() {
        _deleteSuccess.value = null
        _error.value = null
    }

    fun resetUpdateState() {
        _updateSuccess.value = null
        _error.value = null
    }


    fun getEventoById(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            _error.value = null
            _isLoading.value = true

            val urlString = "$backendUrl/$id"
            Log.d("EventiViewModel", "Retrieving evento from $urlString")

            val request = getAuthenticatedRequest(urlString).get().build()


            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                    Log.e("EventiViewModel", "HTTP error: ${response.code}")
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Response evento detail: $responseJson")

                val eventoData: EventoData = gson.fromJson(responseJson, EventoData::class.java)

                _eventoDetail.value = eventoData
            } catch (e: IOException) {
                _error.value = ErrorData(0, _application.getString(R.string.network_error))
                Log.e("EventiViewModel", "Network error: ${e.message}")

            } catch (e: EOFException) {
                _error.value = ErrorData(0, _application.getString(R.string.end_of_stream_error))
                Log.e("EventiViewModel", "End of stream error: ${e.message}")

            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Unexpected error: ${e.message}")

            } finally {
                _isLoading.value = false
            }
        }
    }



    private fun getEventiNoAuth(urlSuffix: String, targetFlow: MutableStateFlow<List<EventoData>?>, category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            targetFlow.value = emptyList()
            _error.value = null
            _isLoading.value = true

            val urlString = "$backendUrl$urlSuffix"

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()


            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                    Log.e("EventiViewModel", "HTTP error: ${response.code} for category: $category")

                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Response $category: $responseJson")

                val eventiList: List<EventoData> = gson.fromJson(responseJson, Array<EventoData>::class.java).toList()


                targetFlow.value = eventiList
            } catch (e: IOException) {
                _error.value = ErrorData(0, _application.getString(R.string.network_error))
                Log.e("EventiViewModel", "Network error: ${e.message}")

            } catch (e: EOFException) {
                _error.value = ErrorData(0, _application.getString(R.string.end_of_stream_error))
                Log.e("EventiViewModel", "End of stream error: ${e.message}")
            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Unexpected error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEventiByNome(nome: String) = getEventiNoAuth("/search?nome=$nome", _eventi, "search")

    fun searchEventiByLuogo(luogo: String) = getEventiNoAuth("/luogo?luogo=$luogo", _eventi, "luogo")




    private fun getEventi(urlSuffix: String, targetFlow: MutableStateFlow<List<EventoData>?>, category: String) {
        CoroutineScope(Dispatchers.IO).launch {
            targetFlow.value = emptyList()
            _error.value = null
            _isLoading.value = true

            val urlString = "$backendUrl$urlSuffix"

            val request = getAuthenticatedRequest(urlString).get().build()


            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                    Log.e("EventiViewModel", "HTTP error: ${response.code} for category: $category")

                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Response $category: $responseJson")

                val eventiList: List<EventoData> = gson.fromJson(responseJson, Array<EventoData>::class.java).toList()


                targetFlow.value = eventiList
            } catch (e: IOException) {
                _error.value = ErrorData(0, _application.getString(R.string.network_error))
                Log.e("EventiViewModel", "Network error: ${e.message}")

            } catch (e: EOFException) {
                _error.value = ErrorData(0, _application.getString(R.string.end_of_stream_error))
                Log.e("EventiViewModel", "End of stream error: ${e.message}")
            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Unexpected error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEventiByDateRange(startDate: String?, endDate: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            _eventi.value = emptyList()
            _error.value = null
            _isLoading.value = true

            val urlString = buildString {
                append("$backendUrl/filtra")
                val params = mutableListOf<String>()

                startDate?.let { params.add("dataInizio=${URLEncoder.encode(it, "UTF-8")}") }
                endDate?.let { params.add("dataFine=${URLEncoder.encode(it, "UTF-8")}") }

                if (params.isNotEmpty()) {
                    append("?${params.joinToString("&")}")
                }
            }

            Log.d("EventiViewModel", "Searching eventi by date range: $urlString")

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                    Log.e("EventiViewModel", "HTTP error: ${response.code}")
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Response date search: $responseJson")

                val eventiList: List<EventoData> = gson.fromJson(responseJson, Array<EventoData>::class.java).toList()
                _eventi.value = eventiList

            } catch (e: IOException) {
                _error.value = ErrorData(0, _application.getString(R.string.network_error))
                Log.e("EventiViewModel", "Network error: ${e.message}")
            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Unexpected error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchEventiAfterDate(date: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _eventi.value = emptyList()
            _error.value = null
            _isLoading.value = true

            val urlString = "$backendUrl/data-after?data=${URLEncoder.encode(date, "UTF-8")}"
            Log.d("EventiViewModel", "Searching eventi after date: $urlString")

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                    Log.e("EventiViewModel", "HTTP error: ${response.code}")
                    return@launch
                }

                val responseJson = response.body?.string() ?: ""
                Log.d("EventiViewModel", "Response after date search: $responseJson")

                val eventiList: List<EventoData> = gson.fromJson(responseJson, Array<EventoData>::class.java).toList()
                _eventi.value = eventiList

            } catch (e: IOException) {
                _error.value = ErrorData(0, _application.getString(R.string.network_error))
                Log.e("EventiViewModel", "Network error: ${e.message}")
            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Unexpected error: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun searchEventiCombined(
        nome: String? = null,
        luogo: String? = null,
        categoriaId: Long? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            _eventi.value = emptyList()
            _error.value = null
            _isLoading.value = true

            try {
                getAllEventiAndFilter(nome, luogo, categoriaId, startDate, endDate)
            } catch (e: Exception) {
                _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
                Log.e("EventiViewModel", "Error in combined search: ${e.message}")
                _isLoading.value = false
            }
        }
    }


    private suspend fun getAllEventiAndFilter(
        nome: String?,
        luogo: String?,
        categoriaId: Long?,
        startDate: String?,
        endDate: String?
    ) {
        try {
            // Prima ottieni tutti gli eventi
            val urlString = "$backendUrl"
            Log.d("EventiViewModel", "Getting all events for filtering: $urlString")

            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urlString)
                .get()
                .addHeader("Accept", "application/json")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                _error.value = ErrorData(response.code, _application.getString(R.string.http_error))
                Log.e("EventiViewModel", "HTTP error: ${response.code}")
                return
            }

            val responseJson = response.body?.string() ?: ""
            Log.d("EventiViewModel", "All events response: $responseJson")

            val allEventi: List<EventoData> = gson.fromJson(responseJson, Array<EventoData>::class.java).toList()

            // Ora applica i filtri lato client
            val filteredEventi = allEventi.filter { evento ->
                var matchesFilters = true

                // Filtro per nome
                nome?.takeIf { it.isNotBlank() }?.let { searchNome ->
                    matchesFilters = matchesFilters &&
                            (evento.nome?.contains(searchNome, ignoreCase = true) == true)
                }

                // Filtro per luogo
                luogo?.takeIf { it.isNotBlank() }?.let { searchLuogo ->
                    matchesFilters = matchesFilters &&
                            (evento.luogo.contains(searchLuogo, ignoreCase = true))
                }

                // Filtro per categoria
                categoriaId?.let { searchCategoriaId ->
                    matchesFilters = matchesFilters &&
                            (evento.categoriaId == searchCategoriaId)
                }

                // Filtro per data di inizio
                startDate?.takeIf { it.isNotBlank() }?.let { searchStartDate ->
                    try {
                        val eventDate = evento.dataOraEvento
                        matchesFilters = matchesFilters &&
                                (eventDate >= searchStartDate)
                    } catch (e: Exception) {
                        Log.e("EventiViewModel", "Error parsing start date: ${e.message}")
                    }
                }

                // Filtro per data di fine
                endDate?.takeIf { it.isNotBlank() }?.let { searchEndDate ->
                    try {
                        val eventDate = evento.dataOraEvento
                        matchesFilters = matchesFilters &&
                                (eventDate <= searchEndDate)
                    } catch (e: Exception) {
                        Log.e("EventiViewModel", "Error parsing end date: ${e.message}")
                    }
                }

                matchesFilters
            }

            Log.d("EventiViewModel", "Filtered ${filteredEventi.size} events from ${allEventi.size} total")
            _eventi.value = filteredEventi

        } catch (e: IOException) {
            _error.value = ErrorData(0, _application.getString(R.string.network_error))
            Log.e("EventiViewModel", "Network error in filtering: ${e.message}")
        } catch (e: Exception) {
            _error.value = ErrorData(0, _application.getString(R.string.unexpected_error))
            Log.e("EventiViewModel", "Unexpected error in filtering: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

}



