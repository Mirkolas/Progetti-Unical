# EasyHome - Guida alla Configurazione

Benvenuto in **EasyHome**, la piattaforma dedicata alla gestione degli immobili. Segui i passaggi di questa guida per configurare e avviare il progetto in locale.

**Nota Importante:** Le **API KEY** non sono incluse nei file scaricabili. Le **API di Google Maps** e **Geocoding** sono limitate a utilizzo solo con **localhost:4200** (cioè Angular). Al momento, la chiave di Google Maps in uso è una chiave di sviluppo. Per utilizzare la chiave corretta, bisogna andare nel file `index.html` e modificare l'uso della chiave API.


---

## Configurazione del Backend

### 1. **Scarica il Backend**
- Accedi al repository [EasyHome-BackEnd](https://github.com/GiuseppeRudi/EasyHome-BackEnd) e scarica il progetto.

### 2. **Imposta le Variabili d'Ambiente**
Aggiungi le seguenti variabili d'ambiente per l'utente corrente:

- **POSTGRES_PASSWORD**: La tua password per il database PostgreSQL.
- **POSTGRES_USER**: Il tuo nome utente per il database PostgreSQL.

**Nota:** Dopo aver configurato le variabili d'ambiente, riavvia il computer per applicare le modifiche.

### 3. **Ripristina il Database**
- Apri **DBeaver** e connettiti al database PostgreSQL.
- Se esiste già una copia del database **EasyHome**, eliminala.
- Crea un nuovo database chiamato **EasyHome**.
- Ripristina il database:
  - Clicca con il tasto destro sul nuovo database e seleziona **Strumenti > Ripristina**.
  - Scegli il formato **pain**, seleziona le opzioni **clean (drop)** e **crea database**.
  - Seleziona il file di backup **.sql** dalla cartella **dump** del Backend.
  - Avvia il ripristino.

### 4. **Avvia il Backend**
Il Backend è ora pronto per essere avviato.

---

## Configurazione del Frontend

### 1. **Installa le Dipendenze**
All'interno della cartella del Frontend, esegui il comando:

   ```bash
   npm install
