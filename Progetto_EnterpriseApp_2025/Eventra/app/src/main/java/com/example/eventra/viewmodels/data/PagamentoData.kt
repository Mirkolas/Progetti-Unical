package com.example.eventra.viewmodels.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PagamentoData(
    val id: Long? = null,
    val nomeTitolare: String,
    val cognomeTitolare: String,
    val numeroCarta: String,
    val scadenza: String,
    val cvv: String,
    val importo: BigDecimal,
    @SerializedName("dataPagamento")
    val dataPagamento: String? = null,
    val stato: String? = null,
    val ordineId: Long? = null
)

data class PagamentoRequest(
    val nomeTitolare: String,
    val cognomeTitolare: String,
    val numeroCarta: String,
    val scadenza: String,
    val cvv: String,
    val importo: String
)