// ==========================================================
// Dr. Sanjit Kumar Physiotherapy Clinic — Frontend logic
// Talks to the Spring Boot REST API under /api/*
// ==========================================================

const API_BASE = ""; // same-origin since frontend is served by Spring Boot

document.addEventListener("DOMContentLoaded", () => {
  const yearEl = document.getElementById("year");
  if (yearEl) yearEl.textContent = new Date().getFullYear();

  setupNavToggle();
  loadConditions();
  setupBookingForm();
  setupPaymentForm();
  setupContactForm();
  setupModal();
});

// ---------- Mobile nav ----------
function setupNavToggle() {
  const toggle = document.getElementById("navToggle");
  const nav = document.querySelector(".site-nav");
  if (!toggle || !nav) return;
  toggle.addEventListener("click", () => nav.classList.toggle("open"));
  nav.querySelectorAll("a").forEach(a => a.addEventListener("click", () => nav.classList.remove("open")));
}

// ---------- Conditions list ----------
let diseaseCache = [];

async function loadConditions() {
  const container = document.getElementById("conditionList");
  if (!container) return;
  try {
    const res = await fetch(`${API_BASE}/api/diseases`);
    if (!res.ok) throw new Error("Failed to load conditions");
    diseaseCache = await res.json();

    if (!diseaseCache.length) {
      container.innerHTML = `<p class="loading-text">Conditions will be listed here soon. Please contact the clinic for details.</p>`;
      return;
    }

    container.innerHTML = diseaseCache.map(d => `
      <div class="condition-card" data-id="${d.id}">
        <span class="condition-tag">${d.severityLevel || "General"}</span>
        <h3>${escapeHtml(d.name)}</h3>
        <p>${escapeHtml(d.shortDescription || "")}</p>
        <p style="font-size:0.8rem; color:var(--forest-mid); font-weight:600;">
          Recovery: ${d.minRecoveryDays}-${d.maxRecoveryDays} days
        </p>
      </div>
    `).join("");

    container.querySelectorAll(".condition-card").forEach(card => {
      card.addEventListener("click", () => openConditionModal(Number(card.dataset.id)));
    });
  } catch (err) {
    container.innerHTML = `<p class="loading-text">Could not load conditions right now. Please refresh or contact the clinic.</p>`;
    console.error(err);
  }
}

function openConditionModal(id) {
  const d = diseaseCache.find(x => x.id === id);
  if (!d) return;

  document.getElementById("modalSeverity").textContent = (d.severityLevel || "General") + " Condition";
  document.getElementById("modalName").textContent = d.name;
  document.getElementById("modalDescription").textContent = d.shortDescription || "";
  document.getElementById("modalTreatment").textContent = d.treatmentApproach || "Personalised treatment plan discussed at consultation.";
  document.getElementById("modalDays").textContent = `${d.minRecoveryDays}-${d.maxRecoveryDays} days`;
  document.getElementById("modalSessions").textContent = d.sessionsPerWeek ?? "-";
  document.getElementById("modalFee").textContent = d.consultationFee ? `₹${d.consultationFee}` : "-";
  document.getElementById("modalSessionFee").textContent = d.sessionFee ? `₹${d.sessionFee}` : "-";

  document.getElementById("modalBookBtn").onclick = () => {
    document.getElementById("conditionModal").classList.add("hidden");
    document.getElementById("relatedConditionInput").value = d.name;
    document.getElementById("book").scrollIntoView({ behavior: "smooth" });
  };

  document.getElementById("conditionModal").classList.remove("hidden");
}

function setupModal() {
  const overlay = document.getElementById("conditionModal");
  const closeBtn = document.getElementById("modalClose");
  if (!overlay || !closeBtn) return;
  closeBtn.addEventListener("click", () => overlay.classList.add("hidden"));
  overlay.addEventListener("click", (e) => {
    if (e.target === overlay) overlay.classList.add("hidden");
  });
}

// ---------- Booking form ----------
function setupBookingForm() {
  const form = document.getElementById("bookingForm");
  if (!form) return;
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = Object.fromEntries(new FormData(form).entries());
    const submitBtn = form.querySelector("button[type=submit]");
    submitBtn.disabled = true;
    submitBtn.textContent = "Submitting…";

    try {
      const res = await fetch(`${API_BASE}/api/appointments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || "Something went wrong");

      document.getElementById("bookSuccess").classList.remove("hidden");
      form.reset();
    } catch (err) {
      alert("Could not submit your appointment request: " + err.message);
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = "Request Appointment";
    }
  });
}

// ---------- Payment form ----------
function setupPaymentForm() {
  const form = document.getElementById("paymentForm");
  if (!form) return;
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const raw = Object.fromEntries(new FormData(form).entries());
    raw.amount = parseFloat(raw.amount);

    const submitBtn = form.querySelector("button[type=submit]");
    submitBtn.disabled = true;
    submitBtn.textContent = "Processing…";

    try {
      const res = await fetch(`${API_BASE}/api/payments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(raw)
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || "Payment could not be recorded");

      const successEl = document.getElementById("paySuccess");
      successEl.textContent = `Payment recorded! Reference ID: ${data.transactionRef}. Please keep this for your records.`;
      successEl.classList.remove("hidden");
      form.reset();
    } catch (err) {
      alert("Payment could not be recorded: " + err.message);
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = "Pay / Record Payment";
    }
  });
}

// ---------- Contact form ----------
function setupContactForm() {
  const form = document.getElementById("contactForm");
  if (!form) return;
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const payload = Object.fromEntries(new FormData(form).entries());
    const submitBtn = form.querySelector("button[type=submit]");
    submitBtn.disabled = true;
    submitBtn.textContent = "Sending…";

    try {
      const res = await fetch(`${API_BASE}/api/contact`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || "Message could not be sent");

      document.getElementById("contactSuccess").classList.remove("hidden");
      form.reset();
    } catch (err) {
      alert("Could not send message: " + err.message);
    } finally {
      submitBtn.disabled = false;
      submitBtn.textContent = "Send Message";
    }
  });
}

// ---------- Helpers ----------
function escapeHtml(str) {
  const div = document.createElement("div");
  div.textContent = str ?? "";
  return div.innerHTML;
}
