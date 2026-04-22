package com.example.eventra.viewmodels.data

data class BigliettoData(
    val id: Long = 0,
    val nomeSpettatore: String = "",
    val cognomeSpettatore: String = "",
    val emailSpettatore: String = "",
    val prezzo: Double = 0.0,
    val eventoId: Long = 0,
    val eventoNome: String = "",
    val tipoPostoId: Long = 0,
    val tipoPostoNome: String = "",
    val dataEvento: String = "",
    val pagamentoId: Long? = null,
    val isExpanded: Boolean = true // Solo per UI
)

data class BigliettoCreateRequest(
    val nomeSpettatore: String,
    val cognomeSpettatore: String,
    val emailSpettatore: String,
    val eventoId: Long,
    val tipoPostoId: Long,
    val pagamentoId: Long
)

data class BigliettoInfoDTO(
    val id: Long,
    val nomeSpettatore: String,
    val cognomeSpettatore: String,
    val emailSpettatore: String,
    val prezzo: Double,
    val eventoNome: String,
    val tipoPostoNome: String,
    val dataEvento: String
)
