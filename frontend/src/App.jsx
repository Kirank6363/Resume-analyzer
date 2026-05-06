import { useState } from "react";

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080").replace(/\/$/, "");

function App() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event) {
    event.preventDefault();

    if (!file) {
      setError("Please choose a resume file before uploading.");
      return;
    }

    setLoading(true);
    setError("");
    setResult(null);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_BASE_URL}/api/resumes/upload`, {
        method: "POST",
        body: formData,
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || "Upload failed. Please try again.");
      }

      setResult(data);
    } catch (uploadError) {
      setError(uploadError.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="page-shell">
      <section className="hero-card">
        <p className="eyebrow">Resume to Job Match</p>
        <h1>Upload a resume and get real matching job links.</h1>
        <p className="hero-copy">
          This frontend talks to your Spring Boot API, uploads the resume as
          multipart form data, and renders the matched jobs with score and
          missing skills.
        </p>

        <form className="upload-form" onSubmit={handleSubmit}>
          <label className="file-picker">
            <span>{file ? file.name : "Choose PDF or DOCX resume"}</span>
            <input
              type="file"
              accept=".pdf,.doc,.docx"
              onChange={(event) => setFile(event.target.files?.[0] ?? null)}
            />
          </label>

          <button className="submit-button" type="submit" disabled={loading}>
            {loading ? "Analyzing resume..." : "Analyze Resume"}
          </button>
        </form>

        {error ? <p className="error-banner">{error}</p> : null}
      </section>

      {result ? (
        <section className="results-grid">
          <article className="panel">
            <h2>Candidate Profile</h2>
            <p className="meta-line">
              <strong>File:</strong> {result.fileName}
            </p>
            <div className="tag-group">
              {result.candidateProfile?.skills?.map((skill) => (
                <span key={skill} className="tag">
                  {skill}
                </span>
              ))}
            </div>
            <p className="section-label">Detected Roles</p>
            <ul>
              {result.candidateProfile?.roles?.map((role) => (
                <li key={role}>{role}</li>
              ))}
            </ul>
            <p className="section-label">Experience Highlights</p>
            <ul>
              {result.candidateProfile?.experienceHighlights?.map((item) => (
                <li key={item}>{item}</li>
              ))}
            </ul>
          </article>

          <article className="panel">
            <h2>Matching Jobs</h2>
            <div className="job-list">
              {result.jobMatches?.map((job) => (
                <section className="job-card" key={`${job.company}-${job.title}`}>
                  <div className="job-heading">
                    <div>
                      <h3>{job.title}</h3>
                      <p>{job.company}</p>
                    </div>
                    <span className="score-pill">{job.matchScore}% match</span>
                  </div>

                  <p className="section-label">Matched Skills</p>
                  <div className="tag-group">
                    {job.matchedSkills?.length ? (
                      job.matchedSkills.map((skill) => (
                        <span key={skill} className="tag success-tag">
                          {skill}
                        </span>
                      ))
                    ) : (
                      <span className="muted-text">No direct skill overlap detected</span>
                    )}
                  </div>

                  <p className="section-label">Missing Skills</p>
                  <div className="tag-group">
                    {job.missingSkills?.length ? (
                      job.missingSkills.map((skill) => (
                        <span key={skill} className="tag warning-tag">
                          {skill}
                        </span>
                      ))
                    ) : (
                      <span className="muted-text">No missing skills identified</span>
                    )}
                  </div>

                  <a
                    className="apply-link"
                    href={job.applyLink}
                    target="_blank"
                    rel="noreferrer"
                  >
                    View Job
                  </a>
                </section>
              ))}
            </div>
          </article>
        </section>
      ) : null}
    </main>
  );
}

export default App;
