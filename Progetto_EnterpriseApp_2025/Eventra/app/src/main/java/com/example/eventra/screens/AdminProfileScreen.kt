package com.example.eventra.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventra.viewmodels.EventiViewModel
import com.example.eventra.viewmodels.BigliettoViewModel
import com.example.eventra.viewmodels.data.EventoData
import com.example.eventra.untils.SessionManager
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.example.eventra.R
import java.text.SimpleDateFormat
import java.util.*
import com.example.eventra.ui.theme.*
import com.example.eventra.viewmodels.StrutturaViewModel
import com.example.eventra.viewmodels.TagCategoriaViewModel
import com.example.eventra.viewmodels.data.StrutturaInfoOrganizzatoreData
import com.example.eventra.viewmodels.data.TagCategoriaData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val eventiViewModel: EventiViewModel = viewModel {
        EventiViewModel(context.applicationContext as Application)
    }

    val bigliettoViewModel: BigliettoViewModel = viewModel {
        BigliettoViewModel(context.applicationContext as Application)
    }

    val strutturaViewModel: StrutturaViewModel = viewModel {
        StrutturaViewModel(context.applicationContext as Application)
    }
    val categoriaViewModel: TagCategoriaViewModel = viewModel {
        TagCategoriaViewModel(context.applicationContext as Application)
    }

    val eventiOrganizzatore by eventiViewModel.eventiByOrganizzatore.collectAsState()
    val isLoading by eventiViewModel.isLoading.collectAsState()
    val bigliettiCount by bigliettoViewModel.bigliettiCount.collectAsState()
    val deleteSuccess by eventiViewModel.deleteSuccess.collectAsState()
    val updateSuccess by eventiViewModel.updateSuccess.collectAsState()
    val createSuccess by eventiViewModel.createSuccess.collectAsState()

    val strutture by strutturaViewModel.strutture.collectAsState()
    val categorie by categoriaViewModel.categorie.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var eventoToDelete by remember { mutableStateOf<EventoData?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var eventoToEdit by remember { mutableStateOf<EventoData?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        val userId = sessionManager.getUserId()
        Log.d("AdminProfileScreen", "Loading events for organizzatore: $userId")
        eventiViewModel.getEventiByOrganizzatore(userId)

        strutturaViewModel.getAllStrutturePerOrganizzatore()
        categoriaViewModel.getAllCategorie()
    }

    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess == true) {
            // Ricarica la lista eventi dopo eliminazione cosi aggiorno la composable
            val userId = sessionManager.getUserId()
            eventiViewModel.getEventiByOrganizzatore(userId)
            eventiViewModel.resetDeleteState()
        }
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            showEditDialog = false
            eventoToEdit = null
            eventiViewModel.resetUpdateState()
        }
    }

    LaunchedEffect(createSuccess) {
        if (createSuccess == true) {
            showCreateDialog = false
            eventiViewModel.resetCreateState()
        }
    }

    LaunchedEffect(eventiOrganizzatore) {
        eventiOrganizzatore?.forEach { evento ->
            bigliettoViewModel.countBigliettiByEvento(evento.id)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EventraColors.BackgroundGray)
        ) {
            AdminProfileHeader(onLogout = onLogout)

            AdminEventsSection(
                events = eventiOrganizzatore ?: emptyList(),
                bigliettiCount = bigliettiCount,
                isLoading = isLoading,

                onDeleteEvent = { evento ->
                    eventoToDelete = evento
                    showDeleteDialog = true
                },

                onEditEvent = { evento ->
                    eventoToEdit = evento
                    showEditDialog = true
                }
            )
        }

        FloatingActionButton(
            onClick = {
                showCreateDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = EventraColors.PrimaryOrange,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_event),
                modifier = Modifier.size(24.dp)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    }

    //dialog per confermare un eliminazione
    if (showDeleteDialog && eventoToDelete != null) {
        DeleteEventDialog(
            evento = eventoToDelete!!,
            onConfirm = {
                eventiViewModel.deleteEvento(
                    eventoId = eventoToDelete!!.id,
                    onSuccess = {
                        Log.d("AdminProfileScreen", "Evento eliminato con successo")
                    },
                    onError = { errorMessage ->
                        Log.e("AdminProfileScreen", "Errore eliminazione: $errorMessage")
                    }
                )
                showDeleteDialog = false
                eventoToDelete = null
            },
            onDismiss = {
                showDeleteDialog = false
                eventoToDelete = null
            }
        )
    }

    //dialog con le modifiche
    if (showEditDialog && eventoToEdit != null) {
        EditEventDialog(
            evento = eventoToEdit!!,
            onConfirm = { nome, descrizione, postiDisponibili, dataOraEvento, dataOraAperturaCancelli ->
                eventiViewModel.updateEvento(
                    eventoOriginale = eventoToEdit!!,
                    nome = nome,
                    descrizione = descrizione,
                    postiDisponibili = postiDisponibili,
                    dataOraEvento = dataOraEvento,
                    dataOraAperturaCancelli = dataOraAperturaCancelli,
                    onSuccess = {
                        Log.d("AdminProfileScreen", "Evento aggiornato con successo")
                    },
                    onError = { errorMessage ->
                        Log.e("AdminProfileScreen", "Errore aggiornamento: $errorMessage")
                    }
                )
            },
            onDismiss = {
                showEditDialog = false
                eventoToEdit = null
            }
        )
    }

    //dialog per fare la creazione
    if (showCreateDialog) {
        CreateEventDialog(
            categorie = categorie ?: emptyList(),
            strutture = strutture,
            onConfirm = { nome, descrizione, categoriaId, immagine, dataOraEvento, dataOraAperturaCancelli, postiDisponibili, luogo, strutturaId ->
                val userId = sessionManager.getUserId()
                eventiViewModel.createEvento(
                    nome = nome,
                    descrizione = descrizione,
                    categoriaId = categoriaId,
                    immagine = immagine,
                    dataOraEvento = dataOraEvento,
                    dataOraAperturaCancelli = dataOraAperturaCancelli,
                    postiDisponibili = postiDisponibili,
                    luogo = luogo,
                    organizzatoreId = userId,
                    strutturaId = strutturaId,
                    onSuccess = { createdEvento ->
                        Log.d("AdminProfileScreen", "Evento creato con successo: ${createdEvento.id}")
                    },
                    onError = { errorMessage ->
                        Log.e("AdminProfileScreen", "Errore creazione: $errorMessage")
                    }
                )
            },
            onDismiss = {
                showCreateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventDialog(
    categorie: List<TagCategoriaData>,
    strutture: List<StrutturaInfoOrganizzatoreData>,
    onConfirm: (String, String, Long, String?, String, String, Int, String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    // Stati per i campi del form
    var nome by remember { mutableStateOf("") }
    var descrizione by remember { mutableStateOf("") }
    var selectedCategoriaId by remember { mutableStateOf<Long?>(null) }
    var immagine by remember { mutableStateOf("") }
    var dataOraEvento by remember { mutableStateOf("") }
    var dataOraAperturaCancelli by remember { mutableStateOf("") }
    var postiDisponibili by remember { mutableStateOf("") }
    var luogo by remember { mutableStateOf("") }
    var selectedStrutturaId by remember { mutableStateOf<Long?>(null) }

    // Stati per dropdown
    var categoriaExpanded by remember { mutableStateOf(false) }
    var strutturaExpanded by remember { mutableStateOf(false) }

    // Stati per validazione
    var nomeError by remember { mutableStateOf(false) }
    var categoriaError by remember { mutableStateOf(false) }
    var postiError by remember { mutableStateOf(false) }
    var luogoError by remember { mutableStateOf(false) }
    var strutturaError by remember { mutableStateOf(false) }
    var dataEventoError by remember { mutableStateOf(false) }
    var dataCancelliError by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        nomeError = nome.isBlank()
        categoriaError = selectedCategoriaId == null
        postiError = postiDisponibili.toIntOrNull()?.let { it <= 0 } ?: true
        luogoError = luogo.isBlank()
        strutturaError = selectedStrutturaId == null
        dataEventoError = dataOraEvento.isBlank()
        dataCancelliError = dataOraAperturaCancelli.isBlank()

        return !nomeError && !categoriaError && !postiError && !luogoError &&
                !strutturaError && !dataEventoError && !dataCancelliError
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EventraColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Crea Nuovo Evento",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = EventraColors.TextDark
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .background(
                                    EventraColors.DividerGray,
                                    shape = CircleShape
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Chiudi",
                                tint = EventraColors.TextGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Campo Nome
                            OutlinedTextField(
                                value = nome,
                                onValueChange = {
                                    nome = it
                                    nomeError = false
                                },
                                label = { Text("Nome Evento *") },
                                isError = nomeError,
                                supportingText = if (nomeError) {
                                    { Text("Il nome è obbligatorio", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Descrizione
                            OutlinedTextField(
                                value = descrizione,
                                onValueChange = { descrizione = it },
                                label = { Text("Descrizione") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {

                            ExposedDropdownMenuBox(
                                expanded = categoriaExpanded,
                                onExpandedChange = {
                                    categoriaExpanded = !categoriaExpanded
                                    categoriaError = false
                                }
                            ) {
                                OutlinedTextField(
                                    value = categorie.find { it.id == selectedCategoriaId }?.nome ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Categoria *") },
                                    isError = categoriaError,
                                    supportingText = if (categoriaError) {
                                        { Text("Seleziona una categoria", color = MaterialTheme.colorScheme.error) }
                                    } else null,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EventraColors.PrimaryOrange,
                                        focusedLabelColor = EventraColors.PrimaryOrange
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = categoriaExpanded,
                                    onDismissRequest = { categoriaExpanded = false }
                                ) {
                                    categorie.forEach { categoria ->
                                        DropdownMenuItem(
                                            text = { Text(categoria.nome ?: "Categoria") },
                                            onClick = {
                                                selectedCategoriaId = categoria.id
                                                categoriaExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            //tendina per la scelta delle strutture dentro il db tipo convenzionate
                            ExposedDropdownMenuBox(
                                expanded = strutturaExpanded,
                                onExpandedChange = {
                                    strutturaExpanded = !strutturaExpanded
                                    strutturaError = false
                                }
                            ) {
                                OutlinedTextField(
                                    value = strutture.find { it.id == selectedStrutturaId }?.nome ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Struttura *") },
                                    isError = strutturaError,
                                    supportingText = if (strutturaError) {
                                        { Text("Seleziona una struttura", color = MaterialTheme.colorScheme.error) }
                                    } else null,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = strutturaExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EventraColors.PrimaryOrange,
                                        focusedLabelColor = EventraColors.PrimaryOrange
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = strutturaExpanded,
                                    onDismissRequest = { strutturaExpanded = false }
                                ) {
                                    strutture.forEach { struttura ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(
                                                        text = struttura.nome,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        text = "${struttura.categoria} - ${struttura.indirizzo}",
                                                        fontSize = 12.sp,
                                                        color = EventraColors.TextGray
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedStrutturaId = struttura.id
                                                strutturaExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            // Campo Luogo
                            OutlinedTextField(
                                value = luogo,
                                onValueChange = {
                                    luogo = it
                                    luogoError = false
                                },
                                label = { Text("Luogo *") },
                                isError = luogoError,
                                supportingText = if (luogoError) {
                                    { Text("Il luogo è obbligatorio", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Posti Disponibili
                            OutlinedTextField(
                                value = postiDisponibili,
                                onValueChange = {
                                    postiDisponibili = it
                                    postiError = false
                                },
                                label = { Text("Posti Disponibili *") },
                                isError = postiError,
                                supportingText = if (postiError) {
                                    { Text("Inserisci un numero valido > 0", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Immagine (opzionale)
                            OutlinedTextField(
                                value = immagine,
                                onValueChange = { immagine = it },
                                label = { Text("URL Immagine (opzionale)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Data e Ora Evento
                            OutlinedTextField(
                                value = dataOraEvento,
                                onValueChange = {
                                    dataOraEvento = it
                                    dataEventoError = false
                                },
                                label = { Text("Data e Ora Evento * (yyyy-MM-dd'T'HH:mm:ss)") },
                                isError = dataEventoError,
                                supportingText = if (dataEventoError) {
                                    { Text("La data evento è obbligatoria", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Data e Ora Apertura Cancelli
                            OutlinedTextField(
                                value = dataOraAperturaCancelli,
                                onValueChange = {
                                    dataOraAperturaCancelli = it
                                    dataCancelliError = false
                                },
                                label = { Text("Apertura Cancelli * (yyyy-MM-dd'T'HH:mm:ss)") },
                                isError = dataCancelliError,
                                supportingText = if (dataCancelliError) {
                                    { Text("La data apertura cancelli è obbligatoria", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        //  Annulla
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = EventraColors.TextGray
                            )
                        ) {
                            Text("Annulla")
                        }

                        //  Crea
                        Button(
                            onClick = {
                                if (validateForm()) {
                                    onConfirm(
                                        nome,
                                        descrizione,
                                        selectedCategoriaId!!,
                                        immagine.takeIf { it.isNotBlank() },
                                        dataOraEvento,
                                        dataOraAperturaCancelli,
                                        postiDisponibili.toInt(),
                                        luogo,
                                        selectedStrutturaId!!
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EventraColors.PrimaryOrange
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Crea Evento", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteEventDialog(
    evento: EventoData,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = stringResource(R.string.confirm_delete_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EventraColors.TextDark
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.confirm_delete_message),
                    fontSize = 14.sp,
                    color = EventraColors.TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "\"${evento.nome}\"",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventraColors.PrimaryOrange
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.confirm_delete_warning),
                    fontSize = 12.sp,
                    color = Color(0xFFD32F2F),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.delete), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = EventraColors.TextGray
                )
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = EventraColors.CardWhite
    )
}
@Composable
fun EditEventDialog(
    evento: EventoData,
    onConfirm: (String, String, Int, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    // Stati per i campi del form
    var nome by remember { mutableStateOf(evento.nome ?: "") }
    var descrizione by remember { mutableStateOf(evento.descrizione ?: "") }
    var postiDisponibili by remember { mutableStateOf(evento.postiDisponibili.toString()) }
    var dataOraEvento by remember { mutableStateOf(evento.dataOraEvento) }
    var dataOraAperturaCancelli by remember { mutableStateOf(evento.dataOraAperturaCancelli) }

    // Stati per validazione
    var nomeError by remember { mutableStateOf(false) }
    var postiError by remember { mutableStateOf(false) }

    fun validateForm(): Boolean {
        nomeError = nome.isBlank()
        postiError = postiDisponibili.toIntOrNull()?.let { it <= 0 } ?: true
        return !nomeError && !postiError
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.8f)
                    .clickable(enabled = false) { }, // Previene la chiusura quando si clicca sulla card
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EventraColors.CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Modifica Evento",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = EventraColors.TextDark
                        )

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .background(
                                    EventraColors.DividerGray,
                                    shape = CircleShape
                                )
                                .size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Chiudi",
                                tint = EventraColors.TextGray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Campo Nome
                            OutlinedTextField(
                                value = nome,
                                onValueChange = {
                                    nome = it
                                    nomeError = false
                                },
                                label = { Text("Nome Evento") },
                                isError = nomeError,
                                supportingText = if (nomeError) {
                                    { Text("Il nome è obbligatorio", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Descrizione
                            OutlinedTextField(
                                value = descrizione,
                                onValueChange = { descrizione = it },
                                label = { Text("Descrizione") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Posti Disponibili
                            OutlinedTextField(
                                value = postiDisponibili,
                                onValueChange = {
                                    postiDisponibili = it
                                    postiError = false
                                },
                                label = { Text("Posti Disponibili") },
                                isError = postiError,
                                supportingText = if (postiError) {
                                    { Text("Inserisci un numero valido > 0", color = MaterialTheme.colorScheme.error) }
                                } else null,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Data e Ora Evento
                            OutlinedTextField(
                                value = dataOraEvento,
                                onValueChange = { dataOraEvento = it },
                                label = { Text("Data e Ora Evento (yyyy-MM-dd'T'HH:mm:ss)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }

                        item {
                            // Campo Data e Ora Apertura Cancelli
                            OutlinedTextField(
                                value = dataOraAperturaCancelli,
                                onValueChange = { dataOraAperturaCancelli = it },
                                label = { Text("Apertura Cancelli (yyyy-MM-dd'T'HH:mm:ss)") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EventraColors.PrimaryOrange,
                                    focusedLabelColor = EventraColors.PrimaryOrange
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = EventraColors.TextGray
                            )
                        ) {
                            Text("Annulla")
                        }

                        Button(
                            onClick = {
                                if (validateForm()) {
                                    onConfirm(
                                        nome,
                                        descrizione,
                                        postiDisponibili.toInt(),
                                        dataOraEvento,
                                        dataOraAperturaCancelli
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EventraColors.PrimaryOrange
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Salva", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminProfileHeader(
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        EventraColors.PrimaryOrange,
                        EventraColors.DarkOrange
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = stringResource(R.string.admin_panel_title),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.admin_panel_subtitle),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = stringResource(R.string.logout),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AdminEventsSection(
    events: List<EventoData>,
    bigliettiCount: Map<Long, Long>,
    isLoading: Boolean,
    onDeleteEvent: (EventoData) -> Unit = {},
    onEditEvent: (EventoData) -> Unit = {}
)  {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "I Miei Eventi",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = EventraColors.TextDark
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = EventraColors.LightOrange)
            ) {
                Text(
                    text = "${events.size} eventi",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = EventraColors.DarkOrange,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (events.isEmpty() && !isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = null,
                        tint = EventraColors.TextGray,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Nessun evento creato",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = EventraColors.TextDark
                    )

                    Text(
                        text = "Tocca il pulsante + per creare il tuo primo evento",
                        fontSize = 14.sp,
                        color = EventraColors.TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(events) { event ->
                    AdminEventCard(
                        event = event,
                        bigliettiVenduti = bigliettiCount[event.id] ?: 0L,
                        onModificaClick = { onEditEvent(event) },
                        onEliminaClick = { onDeleteEvent(event) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminEventCard(
    event: EventoData,
    bigliettiVenduti: Long,
    onModificaClick: () -> Unit,
    onEliminaClick: () -> Unit
) {
    fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateString
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = EventraColors.CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lato sinistro - Biglietti venduti
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Text(
                    text = "$bigliettiVenduti",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventraColors.PrimaryOrange
                )
                Text(
                    text = stringResource(R.string.tickets_sold),
                    fontSize = 12.sp,
                    color = EventraColors.TextGray,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }

            // Centro - Informazioni evento
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = event.nome ?: "Nome evento non disponibile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = EventraColors.TextDark,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = event.luogo,
                    fontSize = 14.sp,
                    color = EventraColors.TextGray,
                    maxLines = 1
                )

                Text(
                    text = formatDate(event.dataOraEvento),
                    fontSize = 12.sp,
                    color = EventraColors.TextGray
                )

                Text(
                    text = "${event.postiDisponibili} posti disponibili",
                    fontSize = 12.sp,
                    color = EventraColors.PrimaryOrange
                )
            }

            // Lato destro - Pulsanti azioni
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onModificaClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EventraColors.SecondaryOrange
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 80.dp, height = 32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Modifica",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Modifica",
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }

                Button(
                    onClick = onEliminaClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(width = 80.dp, height = 32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Elimina",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Elimina",
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
