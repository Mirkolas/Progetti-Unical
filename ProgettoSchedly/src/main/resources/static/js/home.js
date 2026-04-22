document.addEventListener("DOMContentLoaded", () => {
  const promptEl = document.getElementById("prompt");
  const generateBtn = document.getElementById("generatePlan");
  const clearBtn = document.getElementById("clearPlan");
  const toastCd02 = document.getElementById("toast-cd02");

  const todoList = document.getElementById("todoList");
  const doneList = document.getElementById("doneList");
  const todoCount = document.getElementById("todoCount");
  const doneCount = document.getElementById("doneCount");
  const dayPick = document.getElementById("dayPick");
  const dayLabel = document.getElementById("dayLabel");
  const prevDay = document.getElementById("prevDay");
  const nextDay = document.getElementById("nextDay");

  const DAY_ORDER = ["mon","tue","wed","thu","fri"];
  const DAY_LABEL = { mon:"Lun", tue:"Mar", wed:"Mer", thu:"Gio", fri:"Ven" };
  const DAY_LONG  = { mon:"Lunedì", tue:"Martedì", wed:"Mercoledì", thu:"Giovedì", fri:"Venerdì" };
  const DAY_TO_NUM = { mon: 1, tue: 2, wed: 3, thu: 4, fri: 5 };

  let cd02Activities = [];
  let currentUser = null;

  let currentPiano = null;
  let allPiani = [];

  let currentVincoli = null;
  let currentWeekStart = getMondayOfCurrentWeek();

  function getMondayOfCurrentWeek() {
    const today = new Date();
    const day = today.getDay();
    const diff = today.getDate() - day + (day === 0 ? -6 : 1);
    const monday = new Date(today);
    monday.setDate(diff);
    monday.setHours(0, 0, 0, 0);
    return monday;
  }

  function dayToLocalDate(dayName) {
    if (!dayName || !DAY_TO_NUM[dayName]) return null;
    const dayOffset = DAY_TO_NUM[dayName] - 1;
    const date = new Date(currentWeekStart);
    date.setDate(currentWeekStart.getDate() + dayOffset);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  function localDateToDay(dateValue) {
    if (!dateValue) return null;

    let dateString = null;
    if (typeof dateValue === "string") {
      dateString = dateValue;
    } else if (Array.isArray(dateValue) && dateValue.length >= 3) {
      const [y, m, d] = dateValue;
      dateString = `${y}-${String(m).padStart(2, "0")}-${String(d).padStart(2, "0")}`;
    } else {
      return null;
    }

    try {
      const [year, month, day] = dateString.split("-").map(Number);
      const date = new Date(year, month - 1, day);
      const monday = new Date(currentWeekStart);
      const diffDays = Math.floor((date - monday) / (1000 * 60 * 60 * 24));
      if (diffDays < 0 || diffDays > 4) return null;
      return DAY_ORDER[diffDays] || null;
    } catch {
      return null;
    }
  }

  function updateDayLabel() {
    const day = dayPick.value;
    const dayOffset = DAY_TO_NUM[day] - 1;
    const date = new Date(currentWeekStart);
    date.setDate(currentWeekStart.getDate() + dayOffset);
    const months = ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"];
    dayLabel.textContent = `${DAY_LABEL[day]} ${date.getDate()} ${months[date.getMonth()]}`;
  }

  function escapeHtml(text) {
    if (typeof text !== "string") return "";
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
  }

  function cd02AvailableTimes() {
    const mode = document.getElementById("slots").value;
    const morning = ["08:00","09:00","10:00","11:00","12:00"];
    const afternoon = ["13:00","14:00","15:00","16:00","17:00","18:00"];
    if (mode === "morning") return morning;
    if (mode === "afternoon") return afternoon;
    return morning.concat(afternoon);
  }

  function cd02DaysAvailable() {
    const map = { mon:"d-mon", tue:"d-tue", wed:"d-wed", thu:"d-thu", fri:"d-fri" };
    return Object.keys(map).filter(d => document.getElementById(map[d]).checked);
  }

  function cd02MaxPerDay() {
    return Math.max(1, Math.min(6, parseInt(document.getElementById("maxPerDay").value || "1", 10)));
  }

  function cd02DefaultPriority() {
    return document.getElementById("prio").value;
  }

  function parsePromptToDomainActivities(text) {
    const lines = text.split(/\r?\n/).map(s => s.trim()).filter(Boolean);
    const titles = lines
      .map(s => s.startsWith("-") ? s.slice(1).trim() : s)
      .filter(Boolean);

    return titles.map((titolo) => ({
      titolo,
      durata: 60,
      giorno: null,
      fasciaOraria: null,
      scadenza: null,
      priorita: cd02DefaultPriority(),
      note: "Generata da prompt",
      stato: "da completare"
    }));
  }

  function scheduleActivities(activities) {
    const days = cd02DaysAvailable();
    const times = cd02AvailableTimes();
    const maxPerDay = cd02MaxPerDay();

    const perDayCount = { mon:0, tue:0, wed:0, thu:0, fri:0 };
    const usedSlot = new Set();

    const candidates = [];
    for (const day of DAY_ORDER) {
      if (!days.includes(day)) continue;
      for (const time of times) candidates.push({ day, time });
    }

    if (candidates.length === 0) {
      return { placed: 0, total: activities.length, scheduled: [] };
    }

    let placed = 0;
    let candidateIndex = 0;

    for (const act of activities) {
      let chosen = null;
      let attempts = 0;
      const maxAttempts = candidates.length * 2;

      while (!chosen && attempts < maxAttempts) {
        const c = candidates[candidateIndex % candidates.length];
        const key = c.day + "|" + c.time;

        if (perDayCount[c.day] < maxPerDay && !usedSlot.has(key)) chosen = c;

        candidateIndex++;
        attempts++;
      }

      if (!chosen) {
        for (const c of candidates) {
          const key = c.day + "|" + c.time;
          if (!usedSlot.has(key)) { chosen = c; break; }
        }
      }

      if (chosen) {
        act.giorno = dayToLocalDate(chosen.day);
        act.fasciaOraria = chosen.time;
        usedSlot.add(chosen.day + "|" + chosen.time);
        perDayCount[chosen.day] += 1;
        placed += 1;
      }
    }

    return { placed, total: activities.length, scheduled: activities.filter(a => a.giorno) };
  }

  function showToastCd02(msg) {
    const strongEl = toastCd02.querySelector("strong");
    if (strongEl) strongEl.textContent = msg;
    toastCd02.style.display = "flex";
    setTimeout(() => { toastCd02.style.display = "none"; }, 3500);
  }

  function getSelectedPianoId() {
    const sel = document.getElementById("pianoSelect");
    if (!sel) return null;
    const v = sel.value;
    if (!v || v === "all") return null;
    const id = Number(v);
    return Number.isFinite(id) ? id : null;
  }

  async function clearCd02() {
    if (!confirm("Sei sicuro di voler eliminare tutte le attività e i piani?")) return;

    const safeDelete = async (url) => {
      try {
        const r = await fetch(url, { method: "DELETE", cache: "no-store" });
        if (r.status === 401) {
          window.location.href = "/auth/login";
          return { ok: false, auth: false };
        }
        if (r.ok || r.status === 204 || r.status === 404) return { ok: true };
        return { ok: false, status: r.status };
      } catch (e) {
        return { ok: false, error: e };
      }
    };

    try {
      const tasksResponse = await fetch("/api/tasks/current/all", { method: "DELETE", cache: "no-store" });
      if (tasksResponse.status === 401) { window.location.href = "/auth/login"; return; }
      if (!(tasksResponse.ok || tasksResponse.status === 204)) {
        alert("Errore nell'eliminazione delle attività. Status: " + tasksResponse.status);
        return;
      }

      await safeDelete("/api/piano/current/all");
      await safeDelete("/api/piano/current");

      cd02Activities = [];
      currentPiano = null;
      allPiani = [];
      updatePianoSelect();
      const sel = document.getElementById("pianoSelect");
      if (sel) sel.value = "all";

      localStorage.setItem("calendarCleared", JSON.stringify({ timestamp: Date.now() }));

      renderChecklist(dayPick.value);
      showToastCd02("Calendario svuotato con successo!");
      await loadAllPiani();
      await loadTasks();
    } catch (error) {
      console.error("Errore clearCd02:", error);
      alert("Errore nell'eliminazione: " + error.message);
    }
  }

  async function generateCd02() {
    try {
      if (!currentUser) {
        await loadCurrentUser();
        if (!currentUser) { window.location.href = "/auth/login"; return; }
      }

      const raw = parsePromptToDomainActivities(promptEl.value);
      if (raw.length === 0) {
        alert("Inserisci almeno un'attività nel prompt");
        return;
      }

      const { placed, total, scheduled } = scheduleActivities(raw);
      if (scheduled.length === 0) {
        showToastCd02("Nessuna attività pianificata — vincoli troppo stretti");
        renderChecklist(dayPick.value);
        return;
      }

      const selectedPianoId = getSelectedPianoId();
      let pianoIdToUse = selectedPianoId;

      if (!pianoIdToUse) {
        const mode = document.getElementById("mode").value;
        const pianoData = { periodo: mode === "Mese (demo)" ? "MENSILE" : "SETTIMANALE" };

        const pianoResponse = await fetch("/api/piano/current", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(pianoData)
        });

        if (!pianoResponse.ok) {
          if (pianoResponse.status === 401) { window.location.href = "/auth/login"; return; }
          throw new Error("Errore nella creazione del piano (HTTP " + pianoResponse.status + ")");
        }

        currentPiano = await pianoResponse.json();
        pianoIdToUse = currentPiano.id;

        await loadAllPiani();
        const sel = document.getElementById("pianoSelect");
        if (sel) sel.value = String(pianoIdToUse);
      } else {
        currentPiano = allPiani.find(p => Number(p.id) === Number(pianoIdToUse)) || currentPiano;
      }

      if (!pianoIdToUse) {
        alert("Errore: piano non disponibile");
        return;
      }

      scheduled.forEach(task => {
        task.pianoId = pianoIdToUse;
        if (!task.note) task.note = "Generata da prompt";
        if (!task.scadenza) task.scadenza = null;
        task.stato = "da completare";
        if (!task.durata) task.durata = 60;
      });

      const response = await fetch("/api/tasks/current/bulk", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(scheduled)
      });

      if (!response.ok) {
        if (response.status === 401) { window.location.href = "/auth/login"; return; }
        const txt = await response.text();
        throw new Error("Errore nel salvataggio delle attività: " + txt);
      }

      await loadTasks();

      if (placed === total) {
        showToastCd02(selectedPianoId
          ? "Attività aggiunte al piano selezionato!"
          : "Pianificazione generata e salvata con successo!"
        );
      } else {
        showToastCd02(`Pianificazione generata (${placed}/${total}) — vincoli troppo stretti`);
      }

      renderChecklist(dayPick.value);
    } catch (error) {
      console.error("Errore generateCd02:", error);
      alert("Errore nella generazione della pianificazione: " + error.message);
    }
  }

  async function updateTaskStatus(taskId, newStatus) {
    try {
      const task = cd02Activities.find(a => a.id === taskId);
      if (!task) return;

      const updatedTask = { ...task, stato: newStatus };

      const response = await fetch(`/api/tasks/${taskId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedTask)
      });

      if (!response.ok) {
        if (response.status === 401) { window.location.href = "/auth/login"; return; }
        throw new Error("Errore nell'aggiornamento");
      }

      await loadTasks();
    } catch (error) {
      console.error("Errore updateTaskStatus:", error);
      alert("Errore nell'aggiornamento dell'attività.");
    }
  }

  async function editTaskDetails(task) {
    const currentNote = task.note || "";
    const currentScadenza = task.scadenza || "";

    let scadenzaFormatted = "";
    if (currentScadenza) {
      try {
        const date = new Date(currentScadenza + "T00:00:00");
        scadenzaFormatted = date.toISOString().split("T")[0];
      } catch {}
    }

    const noteInput = prompt("Modifica la nota:", currentNote);
    if (noteInput === null) return;

    let scadenzaInput = prompt("Modifica la scadenza (YYYY-MM-DD, vuoto per rimuovere):", scadenzaFormatted);
    if (scadenzaInput === null) return;

    let scadenzaFinale = null;
    if (scadenzaInput && scadenzaInput.trim()) {
      const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
      if (dateRegex.test(scadenzaInput.trim())) {
        scadenzaFinale = scadenzaInput.trim();
      } else {
        alert("Formato data non valido. Usa YYYY-MM-DD (es. 2026-12-31)");
        return;
      }
    }

    try {
      const updatedTask = { ...task, note: noteInput.trim() || null, scadenza: scadenzaFinale };

      const response = await fetch(`/api/tasks/${task.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedTask)
      });

      if (!response.ok) {
        if (response.status === 401) { window.location.href = "/auth/login"; return; }
        throw new Error("Errore nell'aggiornamento");
      }

      await loadTasks();
      showToastCd02("Attività aggiornata con successo!");
    } catch (error) {
      console.error("Errore editTaskDetails:", error);
      alert("Errore nell'aggiornamento dell'attività.");
    }
  }

  function renderChecklist(day) {
    updateDayLabel();

    const todays = cd02Activities.filter(a => localDateToDay(a.giorno) === day);

    const todo = todays.filter(a => {
      const stato = (a.stato || "").toLowerCase();
      return stato !== "completata" && stato !== "done" && stato !== "completato";
    });

    const done = todays.filter(a => {
      const stato = (a.stato || "").toLowerCase();
      return stato === "completata" || stato === "done" || stato === "completato";
    });

    todoCount.textContent = String(todo.length);
    doneCount.textContent = String(done.length);

    todoList.innerHTML = "";
    doneList.innerHTML = "";

    const makeItem = (a, checked) => {
      const wrap = document.createElement("div");
      wrap.className = "todo-item";

      const left = document.createElement("div");
      left.className = "todo-left";

      const cb = document.createElement("input");
      cb.type = "checkbox";
      cb.className = "todo-check";
      cb.checked = checked;

      const taskDay = localDateToDay(a.giorno);
      const scadenzaText = a.scadenza ? new Date(a.scadenza + "T00:00:00").toLocaleDateString("it-IT") : null;

      const body = document.createElement("div");
      body.innerHTML = `
        <div class="todo-title">${escapeHtml(a.titolo || "Senza titolo")}</div>
        <div class="todo-meta">
          <span class="meta-pill">🕒 ${escapeHtml(a.fasciaOraria || "-")}</span>
          <span class="meta-pill">⏱️ ${escapeHtml(String(a.durata || 60))} min</span>
          <span class="meta-pill">⭐ ${escapeHtml(a.priorita || "N/A")}</span>
          <span class="meta-pill">📌 ${escapeHtml(DAY_LONG[taskDay] || "-")}</span>
        </div>
        ${a.note ? `<div class="note">🗒️ ${escapeHtml(a.note)}</div>` : ""}
        ${scadenzaText ? `<div class="scadenza">📅 Scadenza: ${escapeHtml(scadenzaText)}</div>` : ""}
      `;

      left.appendChild(cb);
      left.appendChild(body);

      const right = document.createElement("div");
      right.style.display = "grid";
      right.style.gap = "8px";
      right.style.alignContent = "start";

      const editBtn = document.createElement("button");
      editBtn.className = "small-btn";
      editBtn.type = "button";
      editBtn.textContent = "✏️ Modifica";
      editBtn.addEventListener("click", () => editTaskDetails(a));

      const btn = document.createElement("button");
      btn.className = "small-btn";
      btn.type = "button";
      btn.textContent = checked ? "Ripristina" : "Completa";

      const toggle = async () => {
        const newStatus = checked ? "da completare" : "completata";
        await updateTaskStatus(a.id, newStatus);
        renderChecklist(dayPick.value);
      };

      cb.addEventListener("change", toggle);
      btn.addEventListener("click", toggle);

      right.appendChild(editBtn);
      right.appendChild(btn);

      wrap.appendChild(left);
      wrap.appendChild(right);
      return wrap;
    };

    todo.forEach(a => todoList.appendChild(makeItem(a, false)));
    done.forEach(a => doneList.appendChild(makeItem(a, true)));

    if (todays.length === 0) {
      todoList.innerHTML = `
        <div style="color:var(--muted); font-weight:900; font-size:13px; padding:10px;">
          Nessuna attività pianificata per oggi. Genera la pianificazione o cambia giorno.
        </div>
      `;
    }
  }

  function stepDay(dir) {
    const idx = DAY_ORDER.indexOf(dayPick.value);
    const next = Math.max(0, Math.min(DAY_ORDER.length - 1, idx + dir));
    dayPick.value = DAY_ORDER[next];
    renderChecklist(dayPick.value);
  }

  async function loadCurrentUser() {
    try {
      const response = await fetch("/api/utenti/current");
      if (!response.ok) {
        if (response.status === 401) { window.location.href = "/auth/login"; return; }
        return;
      }
      currentUser = await response.json();
    } catch (error) {
      console.error("Errore caricamento utente:", error);
    }
  }

  async function loadAllPiani() {
    try {
      const response = await fetch("/api/piani/current", { cache: "no-store" });
      if (response.status === 401) { window.location.href = "/auth/login"; return; }

      if (response.ok) {
        allPiani = await response.json();
        allPiani.sort((a, b) => new Date(b.dataCreazione || 0) - new Date(a.dataCreazione || 0));
        updatePianoSelect();

        const sel = document.getElementById("pianoSelect");
        const selected = sel ? sel.value : "all";

        if (selected && selected !== "all") {
          currentPiano = allPiani.find(p => Number(p.id) === Number(selected)) || null;
        } else {
          currentPiano = null;
        }
      } else {
        allPiani = [];
        currentPiano = null;
        updatePianoSelect();
      }
    } catch (error) {
      console.error("Errore caricamento piani:", error);
      allPiani = [];
      currentPiano = null;
      updatePianoSelect();
    }
  }

  function updatePianoSelect() {
    const select = document.getElementById("pianoSelect");
    if (!select) return;

    const currentValue = select.value;

    select.innerHTML = '<option value="all">Tutti i piani</option>';
    allPiani.forEach((piano, index) => {
      const numero = index + 1;
      const date = piano.dataCreazione
        ? new Date(piano.dataCreazione).toLocaleDateString("it-IT", { day: "2-digit", month: "short", year: "numeric" })
        : "Data sconosciuta";

      const option = document.createElement("option");
      option.value = String(piano.id);
      option.textContent = `Piano ${numero} - ${date} (${piano.periodo || "SETTIMANALE"})`;
      select.appendChild(option);
    });

    if (currentValue) select.value = currentValue;
  }

  async function loadTasks() {
    try {
      const selectedPianoId = getSelectedPianoId();

      let tasks = [];
      if (selectedPianoId) {
        const r = await fetch(`/api/piani/${selectedPianoId}/tasks?t=${Date.now()}`, { cache: "no-store" });
        if (r.status === 401) { window.location.href = "/auth/login"; return; }
        if (r.ok) tasks = await r.json();
        else {
          const fallback = await fetch(`/api/tasks/current?t=${Date.now()}`, { cache: "no-store" });
          if (fallback.status === 401) { window.location.href = "/auth/login"; return; }
          tasks = fallback.ok ? await fallback.json() : [];
          tasks = (tasks || []).filter(t => Number(t.pianoId) === Number(selectedPianoId));
        }
      } else {
        const response = await fetch(`/api/tasks/current?t=${Date.now()}`, { cache: "no-store" });
        if (response.status === 401) { window.location.href = "/auth/login"; return; }
        tasks = response.ok ? await response.json() : [];
      }

      cd02Activities = Array.isArray(tasks) ? tasks : [];
      renderChecklist(dayPick.value);
    } catch (error) {
      console.error("Errore caricamento attività:", error);
      cd02Activities = [];
      renderChecklist(dayPick.value);
    }
  }

  async function loadCurrentVincoli() {
    try {
      const response = await fetch("/api/vincoli/current");
      if (response.ok) {
        currentVincoli = await response.json();
        applyVincoliToUI();
      } else if (response.status === 404) {
        currentVincoli = null;
      }
    } catch (error) {
      console.error("Errore caricamento vincoli:", error);
    }
  }

  function applyVincoliToUI() {
    if (!currentVincoli) return;

    if (currentVincoli.fasceOrarieDisponibili) {
      const fasce = currentVincoli.fasceOrarieDisponibili;
      if (fasce.includes("08:00") || fasce.includes("09:00") || fasce.includes("10:00") || fasce.includes("11:00") || fasce.includes("12:00")) {
        if (fasce.includes("13:00") || fasce.includes("14:00") || fasce.includes("15:00") || fasce.includes("16:00") || fasce.includes("17:00") || fasce.includes("18:00")) {
          document.getElementById("slots").value = "both";
        } else {
          document.getElementById("slots").value = "morning";
        }
      } else {
        document.getElementById("slots").value = "afternoon";
      }
    }

    if (currentVincoli.limiteCaricoGiornaliero) {
      document.getElementById("maxPerDay").value = currentVincoli.limiteCaricoGiornaliero;
    }

    if (currentVincoli.giorniPreferiti) {
      const giorni = currentVincoli.giorniPreferiti.split(",").map(g => g.trim());
      const dayMap = { "1": "d-mon", "2": "d-tue", "3": "d-wed", "4": "d-thu", "5": "d-fri" };
      Object.keys(dayMap).forEach(dayNum => {
        const checkbox = document.getElementById(dayMap[dayNum]);
        if (checkbox) checkbox.checked = giorni.includes(dayNum);
      });
    }
  }

  async function saveCurrentVincoli() {
    try {
      const fasceOrarie = cd02AvailableTimes().join(",");
      const giorniPreferiti = cd02DaysAvailable().map(d => {
        const map = { mon: "1", tue: "2", wed: "3", thu: "4", fri: "5" };
        return map[d] || "";
      }).filter(Boolean).join(",");

      const vincoliData = {
        fasceOrarieDisponibili: fasceOrarie,
        giorniPreferiti: giorniPreferiti,
        limiteCaricoGiornaliero: cd02MaxPerDay(),
        impostazioniNotifiche: currentVincoli?.impostazioniNotifiche || "{}"
      };

      const response = await fetch("/api/vincoli/current", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(vincoliData)
      });

      if (response.ok) currentVincoli = await response.json();
    } catch (error) {
      console.error("Errore salvataggio vincoli:", error);
    }
  }

  prevDay.addEventListener("click", () => stepDay(-1));
  nextDay.addEventListener("click", () => stepDay(+1));
  dayPick.addEventListener("change", () => renderChecklist(dayPick.value));
  clearBtn.addEventListener("click", clearCd02);
  generateBtn.addEventListener("click", generateCd02);

  document.getElementById("pianoSelect").addEventListener("change", async (e) => {
    const pianoId = e.target.value;
    if (pianoId && pianoId !== "all") {
      currentPiano = allPiani.find(p => Number(p.id) === Number(pianoId)) || null;
    } else {
      currentPiano = null;
    }
    await loadTasks();
  });

  document.getElementById("slots").addEventListener("change", saveCurrentVincoli);
  document.getElementById("maxPerDay").addEventListener("change", saveCurrentVincoli);
  document.querySelectorAll('input[type="checkbox"][id^="d-"]').forEach(cb => cb.addEventListener("change", saveCurrentVincoli));

  loadCurrentUser()
    .then(() => loadAllPiani())
    .then(() => loadCurrentVincoli())
    .then(() => loadTasks());
});
