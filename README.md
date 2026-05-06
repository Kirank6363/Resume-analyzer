# Resume Analyzer

Resume Analyzer is a resume-to-job matching web application that helps users turn an uploaded resume into relevant job opportunities.

It lets a user:
- upload a PDF or DOCX resume
- extract resume text with Apache Tika
- build a basic candidate profile from skills and role keywords
- fetch live jobs from Adzuna
- show matching job links, match scores, and missing skills

## Live Demo

- Frontend: [https://resume-analyzer-two-ruby.vercel.app/](https://resume-analyzer-two-ruby.vercel.app/)
- Backend API: [https://resume-analyzer-a4pb.onrender.com/](https://resume-analyzer-a4pb.onrender.com/)
- Health Check: [https://resume-analyzer-a4pb.onrender.com/health](https://resume-analyzer-a4pb.onrender.com/health)

## Tech Stack

- Backend: Spring Boot
- Frontend: React + Vite
- Resume parsing: Apache Tika
- Job source: Adzuna API

## Project Structure

```text
resume-analyzer/
  src/                 Spring Boot backend
  frontend/            React frontend
```

## Why the app is structured this way

- The backend owns all business logic: parsing, analysis, Adzuna fetching, and scoring.
- The frontend only handles user interaction and rendering results.
- This separation keeps the system easier to maintain, extend, and deploy.

## Required Environment Variables

### Backend

- `ADZUNA_APP_ID`
- `ADZUNA_APP_KEY`
- `PORT` optional, defaults to `8080`
- `CORS_ALLOWED_ORIGINS` optional, defaults to `http://localhost:5173`

Example:

```powershell
$env:ADZUNA_APP_ID="your_app_id"
$env:ADZUNA_APP_KEY="your_app_key"
$env:CORS_ALLOWED_ORIGINS="http://localhost:5173,https://your-frontend-domain.com"
```

### Frontend

Create `frontend/.env` from `frontend/.env.example`.

- `VITE_API_BASE_URL`

Example:

```text
VITE_API_BASE_URL=http://localhost:8080
```

For deployment, this should point to your hosted backend URL.

## Run Locally

### Backend

```powershell
mvn spring-boot:run
```

### Frontend

```powershell
cd frontend
npm install
npm run dev
```

## Build for Deployment

### Backend

```powershell
mvn clean package
```

This creates the Spring Boot jar in `target/`.

### Frontend

```powershell
cd frontend
npm install
npm run build
```

This creates the production frontend in `frontend/dist/`.

## Deployment Notes

- Deploy the backend and frontend as separate services.
- Put Adzuna credentials only in the backend host environment, not in source code.
- Set `VITE_API_BASE_URL` in the frontend host to your deployed backend URL.
- Set `CORS_ALLOWED_ORIGINS` in the backend host to your deployed frontend URL.

## Architecture Overview

The application flow is:

> The user uploads a resume, the backend extracts the text, builds a candidate profile, fetches live jobs from Adzuna, and scores each job based on skill overlap and role relevance. The React frontend then renders the matching jobs, score, missing skills, and apply links.
