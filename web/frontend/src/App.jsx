
import { useState } from "react";
import "./App.css";

function App() {
  const [text, setText] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // Historique des résumés
  const [history, setHistory] = useState([]);
  const [activeHistoryId, setActiveHistoryId] = useState(null);

  const wordCount = text.trim()
    ? text.trim().split(/\s+/).length
    : 0;

  // Créer un nouveau résumé
  const handleNewSummary = () => {
    setText("");
    setSummary("");
    setError("");
    setLoading(false);
    setActiveHistoryId(null);
  };

  // Consulter un ancien résumé
  const handleSelectHistory = (item) => {
    setText(item.sourceText);
    setSummary(item.summary);
    setError("");
    setActiveHistoryId(item.id);
  };

  // Génération temporaire du résumé
  const handleSummarize = () => {
    if (!text.trim()) {
      setError("Veuillez saisir un texte en arabe.");
      return;
    }

    setError("");
    setSummary("");
    setLoading(true);

    setTimeout(() => {
      const generatedSummary =
        "هذا ملخص تجريبي للنص العربي. سيتم استبداله بالملخص الحقيقي الذي يولده نموذج التعلم العميق.";

      const newItem = {
        id: Date.now(),
        sourceText: text,
        summary: generatedSummary,
        date: new Date().toLocaleString("fr-FR")
      };

      setSummary(generatedSummary);
      setHistory((prev) => [newItem, ...prev]);
      setActiveHistoryId(newItem.id);
      setLoading(false);
    }, 1500);
  };

  return (
    <div className="app-layout">

      {/* SIDEBAR */}
      <aside className="sidebar">

        <div className="sidebar-header">
          <div className="logo">N</div>
          <span>Arabic AI</span>
        </div>

        <button
          className="new-summary-btn"
          onClick={handleNewSummary}
        >
          <span>＋</span> Nouveau résumé
        </button>

        <div className="history-section">
          <h3>HISTORIQUE</h3>

          {history.length === 0 ? (
            <p className="empty-history">
              Aucun résumé pour le moment.
            </p>
          ) : (
            <div className="history-list">
              {history.map((item) => (
                <button
                  key={item.id}
                  className={`history-item ${
                    activeHistoryId === item.id ? "active" : ""
                  }`}
                  onClick={() => handleSelectHistory(item)}
                >
                  <span>▤</span>
                  <span className="history-text">
                    {item.sourceText.slice(0, 25)}...
                  </span>
                </button>
              ))}
            </div>
          )}
        </div>

        <div className="sidebar-footer">
          <span>Arabic Text Summarization</span>
          <small>NLP Deep Learning Project</small>
        </div>

      </aside>

      {/* MAIN CONTENT */}
      <main className="main-content">

        <header className="topbar">
          <span>Arabic Text Summarization</span>
          <span className="status">● AI Summarizer</span>
        </header>

        <div className="workspace">

          <section className="hero">
            <span className="tag">DEEP LEARNING PROJECT</span>

            <h1>تلخيص النصوص العربية</h1>

            <h2>Arabic Text Summarization</h2>

            <p>
              Transformez vos longs textes arabes en résumés
              courts et pertinents grâce à l'intelligence artificielle.
            </p>
          </section>

          <section className="input-card">

            <div className="section-title">
              <h3>Votre texte</h3>
              <span>Texte source en arabe</span>
            </div>

            <textarea
              dir="rtl"
              lang="ar"
              placeholder="أدخل النص العربي هنا..."
              value={text}
              onChange={(e) => {
                setText(e.target.value);
                setSummary("");
                setError("");
                setActiveHistoryId(null);
              }}
            />

            <div className="input-footer">
              <span>{wordCount} mots</span>

              <button
                className="generate-btn"
                onClick={handleSummarize}
                disabled={loading}
              >
                {loading ? "Génération..." : "✦ Générer le résumé"}
              </button>
            </div>

            {error && <p className="error">{error}</p>}

          </section>

          {loading && (
            <div className="loading">
              <div className="spinner"></div>
              <p>Analyse du texte en cours...</p>
            </div>
          )}

          {summary && !loading && (
            <section className="result-card">

              <div className="section-title">
                <h3>Résumé généré</h3>
                <span>Résultat de l'intelligence artificielle</span>
              </div>

              <div className="summary-text" dir="rtl" lang="ar">
                {summary}
              </div>

              <span className="success-label">
                ✓ Résumé généré avec succès
              </span>

            </section>
          )}

        </div>

        <footer>
          NLP Project — Arabic Abstractive Text Summarization
        </footer>

      </main>

    </div>
  );
}

export default App;
