const form = document.getElementById("summaryForm");

const sourceText = document.getElementById("sourceText");

const summarizeBtn = document.getElementById("summarizeBtn");
const clearBtn = document.getElementById("clearBtn");
const copyBtn = document.getElementById("copyBtn");

const counter = document.getElementById("counter");

const loader = document.getElementById("loader");
const error = document.getElementById("error");

const result = document.getElementById("result");
const summary = document.getElementById("summary");

const processingTime = document.getElementById("processingTime");
const tokenCount = document.getElementById("tokenCount");
const tokens = document.getElementById("tokens");


// Compter les mots et les caractères

function updateCounter() {

    const text = sourceText.value.trim();

    const words = text
        ? text.split(/\s+/).length
        : 0;

    counter.textContent =
        `${words} mots · ${sourceText.value.length} caractères`;
}

sourceText.addEventListener("input", updateCounter);


// Effacer le texte

clearBtn.addEventListener("click", function () {

    sourceText.value = "";

    result.hidden = true;
    error.hidden = true;

    updateCounter();

    sourceText.focus();
});


// Générer le résumé

form.addEventListener("submit", async function (event) {

    event.preventDefault();

    const text = sourceText.value.trim();

    result.hidden = true;
    error.hidden = true;

    // Vérification du texte

    if (!text) {

        error.textContent =
            "Veuillez saisir un texte à résumer.";

        error.hidden = false;

        return;
    }

    // Début du chargement

    loader.hidden = false;

    summarizeBtn.disabled = true;
    clearBtn.disabled = true;
    sourceText.disabled = true;

    try {

        // Appel du backend Spring Boot

        const response = await fetch("/api/summarize", {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                text: text
            })

        });

        // Vérifier le code HTTP

        const data = await response.json().catch(() => null);

        if (!response.ok) {

            throw new Error(
                data?.message ||
                `Erreur du serveur (HTTP ${response.status}).`
            );
        }

        if (!data) {

            throw new Error(
                "La réponse du serveur est invalide."
            );
        }

        if (
            data.status !== "success" ||
            typeof data.summary !== "string" ||
            !Array.isArray(data.generated_tokens) ||
            !data.metadata ||
            typeof data.metadata.processing_time_seconds !== "number"
        ) {

            throw new Error(
                "La réponse du serveur est invalide."
            );
        }

        // Afficher le résumé

        summary.textContent = data.summary;

        processingTime.textContent =
            data.metadata.processing_time_seconds
                .toFixed(3) + " s";

        tokenCount.textContent =
            data.generated_tokens.length;

        // Afficher les tokens

        tokens.replaceChildren();

        data.generated_tokens.forEach(function (token) {

            const span = document.createElement("span");

            span.className = "token";
            span.textContent = token;
            span.dir = "auto";

            tokens.appendChild(span);
        });

        result.hidden = false;

    } catch (exception) {

        error.textContent = exception.message;

        error.hidden = false;

    } finally {

        loader.hidden = true;

        summarizeBtn.disabled = false;
        clearBtn.disabled = false;
        sourceText.disabled = false;
    }

});


// Copier le résumé

copyBtn.addEventListener("click", async function () {

    try {

        await navigator.clipboard.writeText(
            summary.textContent
        );

        copyBtn.textContent = "Copié !";

        setTimeout(function () {
            copyBtn.textContent = "Copier";
        }, 1500);

    } catch (exception) {

        error.textContent =
            "Impossible de copier automatiquement le résumé.";

        error.hidden = false;
    }

});


updateCounter();