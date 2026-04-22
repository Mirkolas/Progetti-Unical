package com.example.eventra.viewmodels.data

import com.google.gson.annotations.SerializedName

data class OrdineData(
    val id: Long? = null,
    @SerializedName("dataCreazione")
    val dataCreazione: String? = null,
    val emailProprietario: String,
    val prezzoTotale: Double,
    val proprietarioId: Long? = null
)