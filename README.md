
# Physiotherapy Clinic Website

A full-stack clinic website: a classic green-and-white landing page, an
interior site with facilities, a searchable list of physiotherapy
conditions (with recovery time & fees), an appointment booking form, a fee
payment form, a contact form, and a simple admin dashboard — all backed by
a real MySQL database.

**Stack:** Java 17 + Spring Boot 3 (REST API) · HTML/CSS/JS (server-rendered
as static files) · MySQL · Docker-ready for Render.

---

## 1. Project structure

```
physio-clinic/
├── backend/                          Spring Boot application (this is what you deploy)
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/drsanjitclinic/
│       │   ├── DrSanjitClinicApplication.java
│       │   ├── config/        CorsConfig.java, DataSeeder.java
│       │   ├── model/         Disease, Appointment, Payment, ContactMessage
│       │   ├── repository/    Spring Data JPA repositories
│       │   └── controller/    REST controllers (/api/...)
│       └── resources/
│           ├── application.properties     (all config via env vars)
│           └── static/                    <-- the actual website (HTML/CSS/JS)
│               ├── index.html             landing / intro page
│               ├── clinic.html            main site (facilities, conditions, forms)
│               ├── css/style.css
│               ├── js/main.js
│               └── admin/dashboard.html   owner-only dashboard
├── database/schema.sql               reference SQL (optional, Hibernate auto-creates tables)
├── render.yaml                       Render blueprint
└── README.md
```

The frontend is served **by the Spring Boot app itself** (from
`src/main/resources/static/`), so you deploy **one single app** — no
separate frontend hosting needed.

---

## 2. How the pages fit together

1. **`index.html`** — the first thing a visitor sees: clinic name, tagline,
   address, and a stethoscope-themed background, with an **"Enter Clinic
   Website"** button.
2. **`clinic.html`** — the real site, with sections for About, Facilities,
   Conditions We Treat (loaded live from the database via `/api/diseases`),
   Book Consultation, Pay Fees, and Contact.
3. **`admin/dashboard.html`** — a simple password-protected page (using the
   `ADMIN_KEY` you set) where your father can see every booking, payment,
   and message that came in, plus total revenue.

---

## 3. Running it on your own computer first (recommended before deploying)

### Prerequisites
- Java 17+ (`java -version`)
- Maven (or use the included `mvnw` if you generate one — see note below)
- MySQL 8 running locally (or use XAMPP/WAMP's MySQL)

### Steps
```bash
# 1. Create the database (Hibernate will create the tables automatically)
mysql -u root -p -e "CREATE DATABASE physio_clinic;"

# 2. Set environment variables (or edit application.properties directly for local testing)
export DB_URL="jdbc:mysql://localhost:3306/physio_clinic?useSSL=false&serverTimezone=UTC"
export DB_USERNAME=root
export DB_PASSWORD=yourMySQLPassword
export ADMIN_KEY=myOwnSecretKey123

# 3. Run
cd backend
mvn spring-boot:run
```

Then open:
- `http://localhost:8080/` → landing page
- `http://localhost:8080/clinic.html` → main site
- `http://localhost:8080/admin/dashboard.html` → admin dashboard (enter the `ADMIN_KEY` you set)

> **Note:** This sandbox couldn't reach Maven Central to actually compile the
> project for you (only a short allow-list of package registries is reachable
> here), so please run `mvn spring-boot:run` on your own machine the first
> time to confirm it builds cleanly, before deploying.

---

## 4. Getting a free online MySQL database

Render's own managed MySQL isn't offered on the free tier, so host the
database separately (still free) and point your app at it. Good options:

| Provider | Notes |
|---|---|
| **Railway** | Free MySQL plugin, easiest to set up, gives you a connection URL instantly. |
| **Aiven** | Free tier MySQL, reliable, good for small production apps. |
| **Clever Cloud** | Free MySQL "Dev" plan, simple dashboard. |

Whichever you choose, you'll end up with:
- Host, Port, Database name, Username, Password

Combine them into a JDBC URL:
```
jdbc:mysql://<HOST>:<PORT>/<DATABASE>?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
```

---

## 5. Deploying to Render

1. Push this whole `physio-clinic/` folder to a **GitHub repository**.
2. On [Render](https://render.com), click **New + → Web Service**.
3. Connect your GitHub repo.
4. Render will detect `backend/Dockerfile` — set:
   - **Root Directory:** `backend`
   - **Environment:** Docker
5. Add these **Environment Variables** in the Render dashboard:

   | Key | Value |
   |---|---|
   | `DB_URL` | your online MySQL JDBC URL (see section 4) |
   | `DB_USERNAME` | your database username |
   | `DB_PASSWORD` | your database password |
   | `ADMIN_KEY` | a strong secret only you and your father know |
   | `DDL_AUTO` | `update` |

6. Click **Deploy**. Render builds the Docker image and starts the app.
7. Once live, your site is at `https://your-app-name.onrender.com/`.

> Free Render web services "sleep" after inactivity and take ~30–60 seconds
> to wake up on the next visit. Upgrade to a paid instance later if you want
> the site always instantly responsive.

---

## 6. API reference 

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/diseases` | List all conditions with recovery time & fees |
| POST | `/api/appointments` | Submit a booking (name, phone, date, condition...) |
| POST | `/api/payments` | Record a fee payment |
| POST | `/api/contact` | Submit a contact form message |
| GET | `/api/admin/summary` | Totals (needs `X-Admin-Key` header) |
| GET | `/api/admin/appointments` | All bookings (needs `X-Admin-Key` header) |
| GET | `/api/admin/payments` | All payments (needs `X-Admin-Key` header) |
| GET | `/api/admin/messages` | All contact messages (needs `X-Admin-Key` header) |

---

## 7. Important note about the "Pay Fees" form

The payment form on the site **records** a payment (amount, mode, patient,
reference number) into the database — it does **not** move real money yet.
For genuine online payments (UPI/Card/Net Banking), integrate a payment
gateway such as **Razorpay** or **PayU** (both are popular and well
documented for Indian clinics) inside `PaymentController.java`, and only
mark a payment `SUCCESS` after the gateway confirms it server-side. I kept
this out of the initial build since it needs your own merchant/API keys —
happy to wire it in once you've registered with a gateway.

---

## 8. Customizing content

- **Clinic address, phone, hours:** edit directly in `index.html` and `clinic.html`.
- **Conditions treated, recovery days, fees:** edit the seed list in
  `DataSeeder.java` (only runs once on an empty database — if you've already
  deployed, add/edit rows directly in the `diseases` table instead).
- **Colors/fonts:** all defined as CSS variables at the top of `css/style.css`.
- **Doctor's photo/clinic photos:** drop image files into
  `src/main/resources/static/images/` and reference them in the HTML.

---

## 9. Security notes before going fully live

- Change `ADMIN_KEY` to a long random string — don't leave the default.
- Switch `DDL_AUTO` from `update` to `validate` once your schema is stable,
  so accidental entity changes can't alter the live database structure.
- Consider adding HTTPS-only cookies / real login (Spring Security) for the
  admin dashboard if more than one staff member needs access.
- Add a privacy note near the forms if you plan to store patient health
  information long-term, per applicable healthcare data regulations.
