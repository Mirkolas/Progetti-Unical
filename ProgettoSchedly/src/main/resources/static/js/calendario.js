document.addEventListener("DOMContentLoaded", () => {
  const weekLabel = document.getElementById("weekLabel");
  const prevWeekBtn = document.getElementById("prevWeek");
  const nextWeekBtn = document.getElementById("nextWeek");
  const filterSelect = document.getElementById("filter");
  const addEventBtn = document.getElementById("addEvent");
  const pianoSelectEl = document.getElementById("pianoSelect");
  const deletePianoBtn = document.getElementById("deletePianoBtn");

  let currentWeekStart = getMondayOfCurrentWeek();
  let allTasks = [];
  let allPiani = [];
  let currentPiano = null;
  let currentUser = null;

  let draggedTaskId = null;
  let isDragging = false;

  const DAY_ORDER = ["mon", "tue", "wed", "thu", "fri"];

  const DAY_MAP = {
    1: "mon",
    2: "tue",
    3: "wed",
    4: "thu",
    5: "fri",
  };

  const DAY_LABELS = {
    mon: "Lun",
    tue: "Mar",
    wed: "Mer",
    thu: "Gio",
    fri: "Ven",
  };

  const MONTHS = ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"];

  function getMondayOfCurrentWeek() {
    const today = new Date();
    const day = today.getDay();
    const diff = today.getDate() - day + (day === 0 ? -6 : 1);
    const monday = new Date(today);
    monday.setDate(diff);
    monday.setHours(0, 0, 0, 0);
    return monday;
  }

  function formatDate(date) {
    const d = date.getDate();
    const m = MONTHS[date.getMonth()];
    return `${d} ${m}`;
  }

  function updateWeekLabel() {
    const monday = new Date(currentWeekStart);
    const friday = new Date(monday);
    friday.setDate(monday.getDate() + 4);
    weekLabel.textContent = `${DAY_LABELS.mon} ${formatDate(monday)} - ${DAY_LABELS.fri} ${formatDate(friday)}`;
  }

  function normalizeDateString(dateValue) {
    if (!dateValue) return null;
    if (typeof dateValue === "string") return dateValue;

    if (Array.isArray(dateValue) && dateValue.length >= 3) {
      const [y, m, d] = dateValue;
      const mm = String(m).padStart(2, "0");
      const dd = String(d).padStart(2, "0");
      return `${y}-${mm}-${dd}`;
    }

    return null;
  }

  function getDayOfWeekFromDate(dateValue) {
    const dateString = normalizeDateString(dateValue);
    if (!dateString) return null;

    try {
      const [year, month, day] = dateString.split("-").map(Number);
      const date = new Date(year, month - 1, day);
      const dayOfWeek = date.getDay(); // 0=dom
      const mondayBasedDay = dayOfWeek === 0 ? 7 : dayOfWeek;
      return DAY_MAP[mondayBasedDay] || null;
    } catch (e) {
      console.error("Errore conversione data:", dateValue, e);
      return null;
    }
  }

  function isDateInCurrentWeek(dateValue) {
    const dateString = normalizeDateString(dateValue);
    if (!dateString) return false;

    try {
      const [year, month, day] = dateString.split("-").map(Number);
      const date = new Date(year, month - 1, day);
      const monday = new Date(currentWeekStart);
      monday.setHours(0, 0, 0, 0);
      const friday = new Date(monday);
      friday.setDate(monday.getDate() + 4);
      friday.setHours(23, 59, 59, 999);
      date.setHours(0, 0, 0, 0);
      return date >= monday && date <= friday;
    } catch (e) {
      console.error("Errore controllo settimana:", dateValue, e);
      return false;
    }
  }

  function getPriorityColor(priorita) {
    switch ((priorita || "").toLowerCase()) {
      case "alta":
        return "#ffe4e6";
      case "media":
        return "#dbeafe";
      case "bassa":
        return "#d1fae5";
      default:
        return "#f3f4f6";
    }
  }

  function dayAndTimeToLocalDate(dayName) {
    if (!dayName || !DAY_ORDER.includes(dayName)) return null;
    const dayOffset = DAY_ORDER.indexOf(dayName);
    const date = new Date(currentWeekStart);
    date.setDate(currentWeekStart.getDate() + dayOffset);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  function escapeHtml(text) {
    if (typeof text !== "string") return "";
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
  }

  function showToast(message, type = "success") {
    const toast = document.createElement("div");
    toast.className = `toast-notification ${type}`;
    toast.textContent = message;

    const icon = document.createElement("span");
    icon.textContent = type === "success" ? "✅" : "❌";
    icon.style.marginRight = "8px";
    toast.prepend(icon);

    document.body.appendChild(toast);
    toast.offsetHeight;
    toast.classList.add("show");

    setTimeout(() => {
      toast.classList.remove("show");
      setTimeout(() => {
        if (toast.parentNode) toast.parentNode.removeChild(toast);
      }, 300);
    }, 2500);
  }

  function renderCalendar() {
    document.querySelectorAll(".calendar-cell").forEach((cell) => {
      cell.innerHTML = "";
    });

    const filteredTasks = allTasks.filter((task) => {
      const stato = (task.stato || "").toLowerCase();
      if (stato === "completata" || stato === "done" || stato === "completato") return false;

      if (currentPiano && currentPiano.id) {
        if (String(task.pianoId) !== String(currentPiano.id)) return false;
      }

      if (!isDateInCurrentWeek(task.giorno)) return false;

      const filterValue = filterSelect.value;
      if (filterValue !== "all") {
        const taskPriority = (task.priorita || "").toLowerCase();
        if (filterValue === "alta" && taskPriority !== "alta") return false;
        if (filterValue === "media" && taskPriority !== "media") return false;
        if (filterValue === "bassa" && taskPriority !== "bassa") return false;
      }

      return true;
    });

    filteredTasks.forEach((task) => {
      const day = getDayOfWeekFromDate(task.giorno);
      if (!day || !task.fasciaOraria) return;

      const cell = document.querySelector(
        `.calendar-cell[data-day="${day}"][data-time="${task.fasciaOraria}"]`
      );
      if (!cell) return;

      const eventDiv = document.createElement("div");
      eventDiv.className = "calendar-event";
      eventDiv.style.backgroundColor = getPriorityColor(task.priorita);
      eventDiv.dataset.taskId = String(task.id);
      eventDiv.setAttribute("draggable", "true");
      eventDiv.setAttribute("title", task.note || task.titolo);

      eventDiv.innerHTML = `
        <div class="event-title">${escapeHtml(task.titolo || "Senza titolo")}</div>
        <div class="event-priority">${escapeHtml(task.priorita || "N/A")}</div>
      `;

      eventDiv.addEventListener("dragstart", (e) => {
        isDragging = true;
        draggedTaskId = task.id;
        e.dataTransfer.effectAllowed = "move";
        e.dataTransfer.setData("text/plain", String(task.id));
        eventDiv.classList.add("dragging");
      });

      eventDiv.addEventListener("dragend", () => {
        isDragging = false;
        draggedTaskId = null;
        eventDiv.classList.remove("dragging");
        document.querySelectorAll(".calendar-cell").forEach((c) => c.classList.remove("drag-over"));
      });

      eventDiv.addEventListener("click", (e) => {
        if (isDragging) {
          e.preventDefault();
          e.stopPropagation();
          return;
        }
        e.stopPropagation();
        showTaskModal(task);
      });

      cell.appendChild(eventDiv);
    });

    setupCellDropHandlers();
  }

  function setupCellDropHandlers() {
    document.querySelectorAll(".calendar-cell").forEach((cell) => {
      if (cell._dropHandlers) {
        cell.removeEventListener("dragenter", cell._dropHandlers.dragenter);
        cell.removeEventListener("dragover", cell._dropHandlers.dragover);
        cell.removeEventListener("dragleave", cell._dropHandlers.dragleave);
        cell.removeEventListener("drop", cell._dropHandlers.drop);
      }

      const dragenterHandler = (e) => {
        e.preventDefault();
        cell.classList.add("drag-over");
      };

      const dragoverHandler = (e) => {
        e.preventDefault();
        e.dataTransfer.dropEffect = "move";
        cell.classList.add("drag-over");
      };

      const dragleaveHandler = (e) => {
        if (!cell.contains(e.relatedTarget)) cell.classList.remove("drag-over");
      };

      const dropHandler = async (e) => {
        e.preventDefault();
        e.stopPropagation();
        cell.classList.remove("drag-over");

        isDragging = false;

        let taskId = parseInt(e.dataTransfer.getData("text/plain"), 10);
        if (!taskId || Number.isNaN(taskId)) taskId = draggedTaskId;

        if (!taskId) {
          showToast("Errore: taskId mancante", "error");
          draggedTaskId = null;
          return;
        }

        const day = cell.getAttribute("data-day");
        const time = cell.getAttribute("data-time");
        if (!day || !time) {
          showToast("Errore: cella non valida", "error");
          draggedTaskId = null;
          return;
        }

        const task = allTasks.find((t) => String(t.id) === String(taskId));
        if (!task) {
          showToast("Attività non trovata, ricarico...", "error");
          draggedTaskId = null;
          await loadTasks();
          return;
        }

        const currentDay = getDayOfWeekFromDate(task.giorno);
        if (currentDay === day && task.fasciaOraria === time) {
          showToast("L'attività è già qui", "error");
          draggedTaskId = null;
          return;
        }

        const newDate = dayAndTimeToLocalDate(day);
        if (!newDate) {
          showToast("Errore conversione data", "error");
          draggedTaskId = null;
          return;
        }

        const conflictingTask = allTasks.find((t) => {
          if (String(t.id) === String(taskId)) return false;
          const tDay = getDayOfWeekFromDate(t.giorno);
          return tDay === day && t.fasciaOraria === time && (!currentPiano || String(t.pianoId) === String(currentPiano.id));
        });

        if (conflictingTask) {
          const ok = confirm(
            `⚠️ SLOT OCCUPATO!\n\n` +
              `Attività esistente:\n"${conflictingTask.titolo}" (${conflictingTask.priorita})\n\n` +
              `Vuoi spostare "${task.titolo}" qui?\n` +
              `Le attività si sovrapporranno.`
          );
          if (!ok) {
            draggedTaskId = null;
            return;
          }
        }

        await updateTaskPosition(taskId, newDate, time);

        draggedTaskId = null;
        isDragging = false;
      };

      cell.addEventListener("dragenter", dragenterHandler);
      cell.addEventListener("dragover", dragoverHandler);
      cell.addEventListener("dragleave", dragleaveHandler);
      cell.addEventListener("drop", dropHandler);

      cell._dropHandlers = {
        dragenter: dragenterHandler,
        dragover: dragoverHandler,
        dragleave: dragleaveHandler,
        drop: dropHandler,
      };
    });
  }

  async function updateTaskPosition(taskId, newDate, newTime) {
    try {
      const task = allTasks.find((t) => String(t.id) === String(taskId));
      if (!task) return;

      const updatedTask = {
        id: task.id,
        titolo: task.titolo,
        durata: task.durata,
        giorno: newDate,
        fasciaOraria: newTime,
        scadenza: task.scadenza,
        priorita: task.priorita,
        note: task.note,
        stato: task.stato,
        utenteId: task.utenteId,
        pianoId: task.pianoId
      };

      const response = await fetch(`/api/tasks/${taskId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify(updatedTask)
      });

      if (!response.ok) {
        if (response.status === 401) {
          window.location.href = "/auth/login";
          return;
        }
        const errorText = await response.text();
        throw new Error(`HTTP ${response.status}: ${errorText}`);
      }

      await loadTasks();
      showToast("✅ Attività spostata con successo!");
    } catch (error) {
      console.error("❌ Errore updateTaskPosition:", error);
      showToast("❌ Errore: " + error.message, "error");
      await loadTasks();
    }
  }

  function showTaskModal(task) {
    const confirmDelete = confirm(
      `Attività: ${task.titolo}\n` +
      `Priorità: ${task.priorita}\n` +
      `Nota: ${task.note || "Nessuna"}\n\n` +
      `Vuoi eliminare?\n\nOK = Elimina | Annulla = Chiudi`
    );
    if (confirmDelete) deleteTask(task.id);
  }

  async function deleteTask(taskId) {
    try {
      const response = await fetch(`/api/tasks/${taskId}`, { method: "DELETE" });
      if (!response.ok && response.status !== 204) {
        if (response.status === 401) {
          window.location.href = "/auth/login";
          return;
        }
        throw new Error("Errore eliminazione");
      }
      await loadTasks();
      showToast("🗑️ Attività eliminata!");
    } catch (error) {
      console.error("❌ Errore eliminazione:", error);
      showToast("❌ Errore eliminazione", "error");
    }
  }

  async function loadTasks() {
    try {
      let url = `/api/tasks/current?t=${Date.now()}`;

      if (currentPiano && currentPiano.id) {
        url = `/api/piani/${currentPiano.id}/tasks?t=${Date.now()}`;
      }

      const response = await fetch(url, { cache: "no-store" });

      if (!response.ok) {
        if (response.status === 401) {
          window.location.href = "/auth/login";
          return;
        }
        if (response.status === 404) {
          allTasks = [];
          renderCalendar();
          return;
        }
        throw new Error(`Errore: ${response.status}`);
      }

      const tasks = await response.json();
      allTasks = Array.isArray(tasks) ? tasks : [];
      renderCalendar();
    } catch (error) {
      console.error("❌ Errore caricamento tasks:", error);
      allTasks = [];
      renderCalendar();
    }
  }

  async function loadCurrentUser() {
    try {
      const response = await fetch("/api/utenti/current");
      if (!response.ok) {
        if (response.status === 401) {
          window.location.href = "/auth/login";
          return;
        }
      } else {
        currentUser = await response.json();
      }
    } catch (error) {
      console.error("❌ Errore caricamento utente:", error);
    }
  }

  async function loadAllPiani() {
    try {
      const response = await fetch(`/api/piani/current?t=${Date.now()}`, { cache: "no-store" });
      if (response.status === 401) {
        window.location.href = "/auth/login";
        return;
      }

      if (response.ok) {
        allPiani = await response.json();
        allPiani.sort((a, b) => new Date(b.dataCreazione || 0) - new Date(a.dataCreazione || 0));
        updatePianoSelect();

        currentPiano = null;
        pianoSelectEl.value = "all";
        if (deletePianoBtn) deletePianoBtn.style.display = "none";
      } else if (response.status === 404) {
        allPiani = [];
        updatePianoSelect();
        currentPiano = null;
        pianoSelectEl.value = "all";
        if (deletePianoBtn) deletePianoBtn.style.display = "none";
      }
    } catch (error) {
      console.error("❌ Errore caricamento piani:", error);
      allPiani = [];
      updatePianoSelect();
      currentPiano = null;
      pianoSelectEl.value = "all";
      if (deletePianoBtn) deletePianoBtn.style.display = "none";
    }
  }

  function updatePianoSelect() {
    pianoSelectEl.innerHTML = '<option value="all">Tutti i piani</option>';
    allPiani.forEach((piano, index) => {
      const pianoNumber = index + 1;
      const date = piano.dataCreazione
        ? new Date(piano.dataCreazione).toLocaleDateString("it-IT", { day: "2-digit", month: "short", year: "numeric" })
        : "Data sconosciuta";

      const option = document.createElement("option");
      option.value = String(piano.id);
      option.textContent = `Piano ${pianoNumber} - ${date} (${piano.periodo || "SETTIMANALE"})`;
      pianoSelectEl.appendChild(option);
    });
  }

  async function createTask(taskData) {
    try {
      let url = "/api/tasks/current/bulk"; // fallback legacy

      if (currentPiano && currentPiano.id) {
        url = `/api/piani/${currentPiano.id}/tasks/bulk`;
        taskData.pianoId = currentPiano.id; // coerenza DTO
      }

      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify([taskData]),
      });

      if (!response.ok) {
        if (response.status === 401) {
          window.location.href = "/auth/login";
          return;
        }
        const t = await response.text();
        throw new Error(t || "Errore creazione");
      }

      await loadTasks();
      showToast("✅ Attività creata!");
    } catch (error) {
      console.error("❌ Errore createTask:", error);
      showToast("❌ Errore creazione: " + error.message, "error");
    }
  }

  // UI add event
  addEventBtn.addEventListener("click", () => {
    const titolo = prompt("Titolo:");
    if (!titolo || !titolo.trim()) return;

    const priorita = prompt("Priorità (Alta/Media/Bassa):", "Media");
    const durata = parseInt(prompt("Durata (minuti):", "60"), 10) || 60;
    const giorno = prompt("Giorno (YYYY-MM-DD):");
    const fasciaOraria = prompt("Fascia oraria (es. 09:00):");

    if (giorno && fasciaOraria) {
      createTask({
        titolo: titolo.trim(),
        durata,
        giorno,
        fasciaOraria,
        priorita: priorita || "Media",
        note: "Aggiunta manualmente",
        stato: "da completare",
      });
    }
  });

  prevWeekBtn.addEventListener("click", () => {
    currentWeekStart = new Date(currentWeekStart);
    currentWeekStart.setDate(currentWeekStart.getDate() - 7);
    currentWeekStart.setHours(0, 0, 0, 0);
    updateWeekLabel();
    renderCalendar();
  });

  nextWeekBtn.addEventListener("click", () => {
    currentWeekStart = new Date(currentWeekStart);
    currentWeekStart.setDate(currentWeekStart.getDate() + 7);
    currentWeekStart.setHours(0, 0, 0, 0);
    updateWeekLabel();
    renderCalendar();
  });

  filterSelect.addEventListener("change", () => renderCalendar());

  // cambio piano
  pianoSelectEl.addEventListener("change", async (e) => {
    const pianoId = e.target.value;
    if (pianoId && pianoId !== "all") {
      currentPiano = allPiani.find((p) => String(p.id) === String(pianoId)) || null;
      if (deletePianoBtn) deletePianoBtn.style.display = "inline-block";
    } else {
      currentPiano = null;
      if (deletePianoBtn) deletePianoBtn.style.display = "none";
    }
    await loadTasks();
  });

  // init
  updateWeekLabel();
  setupCellDropHandlers();

  loadCurrentUser()
    .then(() => loadAllPiani())
    .then(() => loadTasks());
});
