function escapeHtml(s){
  return String(s)
      .replaceAll("&","&amp;")
      .replaceAll("<","&lt;")
      .replaceAll(">","&gt;")
      .replaceAll('"',"&quot;")
      .replaceAll("'","&#039;");
}

document.addEventListener("DOMContentLoaded", () => {
  document.querySelectorAll("[data-close]").forEach(x => {
    x.addEventListener("click", () => {
      const sel = x.getAttribute("data-close");
      const t = document.querySelector(sel);
      if (t) t.style.display = "none";
    });
  });

  document.querySelectorAll(".input span[title='mostra']").forEach(eye => {
    eye.addEventListener("click", () => {
      const input = eye.parentElement.querySelector("input[type='password'], input[type='text']");
      if (input) {
        if (input.type === "password") {
          input.type = "text";
          eye.textContent = "🙈";
        } else {
          input.type = "password";
          eye.textContent = "👁️";
        }
      }
    });
  });
});
