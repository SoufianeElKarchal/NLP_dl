
# Web Application — Arabic Text Summarization

## 1. Présentation

Cette partie du projet concerne le développement d'une application web permettant d'exploiter un modèle de résumé automatique de textes arabes.

L'application est composée d'un frontend développé en HTML, CSS et JavaScript, et d'un backend développé avec Spring Boot.

Le backend communique avec le service NLP Python via une API REST afin de récupérer les résumés générés par le modèle Deep Learning.

## 2. Fonctionnalités

- Saisie de textes arabes.
- Génération automatique de résumés.
- Affichage du résumé généré.
- Affichage des tokens générés.
- Affichage du temps de traitement.
- Comptage des mots et des caractères.
- Copie du résumé.
- Validation des données entrantes.
- Gestion des erreurs et des délais d'attente.
- Prise en charge de l'encodage UTF-8.
- Affichage du texte arabe de droite à gauche (RTL).
- Interface responsive.

## 3. Technologies utilisées

| Technologie        | Rôle                          |
| ------------------ | ------------------------------ |
| Java 17            | Développement backend         |
| Spring Boot 4.2.0  | Framework backend              |
| Spring MVC         | Gestion des API REST           |
| Maven Wrapper      | Gestion des dépendances       |
| RestClient         | Communication avec FastAPI     |
| Jackson            | Conversion Java / JSON         |
| Jakarta Validation | Validation des données        |
| HTML5              | Structure de l'interface       |
| CSS3               | Style de l'interface           |
| JavaScript         | Interactions et requêtes HTTP |
| JUnit              | Tests automatisés             |
| Mockito            | Simulation des dépendances    |
| MockMvc            | Tests des Controllers          |

## 4. Architecture logicielle

```text
         Utilisateur
              |
              v
          Frontend
   HTML / CSS / JavaScript
              |
         HTTP / JSON
              |
              v
   Spring Boot - Port 8080
              |
      SummaryController
              |
       SummaryService
              |
         RestClient
              |
         HTTP / JSON
              |
              v
    FastAPI - Port 8000
              |
       Modèle NLP
```

Le frontend communique avec Spring Boot, qui transmet les requêtes au service NLP via FastAPI.

Cette architecture permet de séparer l'interface utilisateur, la logique applicative et le traitement Deep Learning.

## 5. Structure du dossier web

```text
web/
├── backend/
│   ├── .mvn/
│   ├── .gitignore
│   ├── .gitattributes
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── src/
│       ├── main/
│       │   ├── java/com/nlp/summarization/
│       │   │   ├── SummarizationApplication.java
│       │   │   ├── controller/
│       │   │   │   ├── HealthController.java
│       │   │   │   └── SummaryController.java
│       │   │   ├── dto/
│       │   │   │   ├── SummaryRequest.java
│       │   │   │   └── SummaryResponse.java
│       │   │   ├── service/
│       │   │   │   └── SummaryService.java
│       │   │   └── exception/
│       │   │       └── GlobalExceptionHandler.java
│       │   └── resources/
│       │       ├── application.properties
│       │       └── static/
│       │           ├── index.html
│       │           ├── style.css
│       │           └── script.js
│       └── test/
│
├── mock/
│   ├── mock_api.py
│   └── requirements.txt
│
└── README.md
```

Le dossier `mock/` contient un service de simulation utilisé uniquement pour le développement et les tests indépendants du modèle NLP.

## 6. Composants du backend

| Composant              | Responsabilité                               |
| ---------------------- | --------------------------------------------- |
| HealthController       | Vérification de la disponibilité du backend |
| SummaryController      | Réception et validation des requêtes        |
| SummaryService         | Communication avec le service NLP             |
| SummaryRequest         | Représentation des données entrantes        |
| SummaryResponse        | Représentation des résultats                |
| GlobalExceptionHandler | Gestion centralisée des exceptions           |

## 7. Prérequis

- Java JDK 17.
- Python 3 compatible avec le service NLP.
- Git.
- Navigateur web.

Maven Wrapper est fourni avec le projet. Il n'est pas nécessaire d'installer Maven globalement.

Vérification des installations :

```powershell
java -version

javac -version

python --version

python -m pip --version

git --version
```

## 8. Installation des dépendances

### 8.1. Dépendances du service Python

Depuis la racine du repository :

```powershell
python -m venv .venv

.\.venv\Scripts\Activate.ps1

python -m pip install --upgrade pip

python -m pip install -r requirements.txt
```

Vérifier les dépendances installées :

```powershell
python -m pip check
```

Les dépendances du modèle et de son API doivent être déclarées dans le fichier `requirements.txt` du projet ou dans le fichier de dépendances indiqué par la documentation du modèle.

### 8.2. Dépendances Java

Depuis la racine du repository :

```powershell
cd web/backend
```

Vérifier Maven Wrapper :

```powershell
.\mvnw.cmd -version
```

Télécharger les dépendances :

```powershell
.\mvnw.cmd dependency:resolve
```

Compiler le backend :

```powershell
.\mvnw.cmd compile
```

Les dépendances Java sont définies dans le fichier `pom.xml` et téléchargées automatiquement par Maven.

Aucune installation de dépendances supplémentaires n'est nécessaire pour le frontend.

## 9. Configuration

Le fichier de configuration du backend est situé dans :

```text
web/backend/src/main/resources/application.properties
```

Configuration par défaut :

```properties
spring.application.name=summarization
server.port=8080
model.api.url=http://127.0.0.1:8000
model.api.read-timeout-ms=60000
```

| Propriété               | Description                                  |
| ------------------------- | -------------------------------------------- |
| server.port               | Port du backend Java                         |
| model.api.url             | Adresse du service NLP                       |
| model.api.read-timeout-ms | Délai d'attente de lecture en millisecondes |

## 10. Démarrage de l'application

Deux serveurs doivent fonctionner simultanément.

### Terminal 1 — Service NLP Python

Depuis la racine du repository :

```powershell
.\.venv\Scripts\Activate.ps1
```

Démarrer le service FastAPI du modèle.

Exemple si l'instance FastAPI est définie dans `model/api.py` :

```powershell
python -m uvicorn model.api:app --host 127.0.0.1 --port 8000
```

Adapter cette commande au point d'entrée réel du service NLP indiqué dans la documentation du modèle.

Adresse du service :

http://127.0.0.1:8000

Documentation Swagger :

http://127.0.0.1:8000/docs

### Terminal 2 — Backend Spring Boot

Depuis la racine du repository :

```powershell
cd web/backend

.\mvnw.cmd spring-boot:run
```

Sous Linux ou macOS :

```bash
cd web/backend

./mvnw spring-boot:run
```

### Accéder à l'interface web

Ouvrir dans le navigateur :

http://localhost:8080/

Saisir un texte arabe puis cliquer sur « Générer le résumé ».

Le frontend est directement servi par Spring Boot. Il ne nécessite pas de serveur web supplémentaire.

## 11. Documentation des API REST

### API Spring Boot

Adresse de base : `http://localhost:8080`

| Méthode | Endpoint           | Description                            |
| -------- | ------------------ | -------------------------------------- |
| GET      | `/api/health`    | Vérifier la disponibilité du backend |
| POST     | `/api/summarize` | Générer un résumé                  |

### API du service NLP

Adresse de base : `http://127.0.0.1:8000`

| Méthode | Endpoint              | Description                     |
| -------- | --------------------- | ------------------------------- |
| POST     | `/api/v1/summarize` | Demander un résumé au modèle |

### Structure de la requête

```json
{
    "text": "أعلنت الحكومة المغربية عن مشروع جديد لتطوير التعليم الرقمي."
}
```

### Structure de la réponse

Exemple illustratif :

```json
{
    "status": "success",
    "summary": "المغرب يطور التعليم الرقمي",
    "generated_tokens": [
        "المغرب",
        "يطور",
        "التعليم",
        "الرقمي",
        "<EOS>"
    ],
    "metadata": {
        "processing_time_seconds": 1.25
    }
}
```

Le résumé et le temps de traitement dépendent du texte fourni et du modèle utilisé.

## 12. Gestion des erreurs

Le backend utilise `GlobalExceptionHandler` pour centraliser la gestion des principales exceptions.

| Code HTTP | Description                                |
| --------- | ------------------------------------------ |
| 200       | Traitement réussi                         |
| 400       | Requête invalide                          |
| 405       | Méthode HTTP non autorisée               |
| 502       | Erreur ou réponse invalide du service NLP |
| 503       | Service NLP indisponible                   |
| 504       | Délai d'attente dépassé                 |

Exemple de réponse d'erreur :

```json
{
    "status": "error",
    "code": 503,
    "message": "Le service NLP est indisponible."
}
```

Les messages d'erreur sont affichés directement dans l'interface utilisateur.

## 13. Tests automatisés

Depuis le dossier `web/backend` :

```powershell
.\mvnw.cmd test
```

Les tests habituels vérifient :

- Le démarrage du contexte Spring.
- La disponibilité du backend.
- La sérialisation et la désérialisation JSON.
- La validation des données.
- Le fonctionnement des Controllers.
- La gestion des exceptions.

### Tests d'intégration

Démarrer le service Python de test et Spring Boot, puis exécuter :

```powershell
.\mvnw.cmd test "-Dtest=SummaryServiceIT,SummaryControllerIT" "-DargLine=-Dfile.encoding=UTF-8"
```

Les tests d'intégration vérifient la communication HTTP entre Java et Python ainsi que la conservation des caractères arabes.

Les tests utilisant un résumé fixe doivent être exécutés avec le Mock. Pour tester le modèle réel, les assertions doivent vérifier la conformité du contrat API sans imposer le résultat du Mock.

## 14. Utilisation du Mock pour le développement

Le Mock permet de tester l'application web indépendamment du modèle Deep Learning.

Depuis la racine du repository :

```powershell
cd web/mock

python -m venv .venv

.\.venv\Scripts\Activate.ps1

python -m pip install -r requirements.txt

python mock_api.py
```

Le Mock utilise le port 8000 et respecte le même contrat API que le service NLP.

Il retourne un résumé fixe et ne réalise pas de véritable traitement Deep Learning.

Le Mock et le service NLP réel ne doivent pas être lancés simultanément sur le même port.

## 15. Construction du fichier JAR

Depuis `web/backend` :

```powershell
.\mvnw.cmd clean package
```

Le fichier exécutable généré est :

```text
target/summarization-0.0.1-SNAPSHOT.jar
```

Pour démarrer le backend sans Maven :

```powershell
java -jar target/summarization-0.0.1-SNAPSHOT.jar
```

Le service NLP doit être démarré séparément.

## 16. Arrêt de l'application

Arrêter Spring Boot et FastAPI avec `Ctrl+C` dans leurs terminaux respectifs.

Pour désactiver l'environnement virtuel Python :

```powershell
deactivate
```

## 17. Dépannage

### Port 8080 déjà utilisé

Identifier le processus occupant le port :

```powershell
netstat -ano | findstr :8080
```

Arrêter l'ancienne instance Spring Boot avant de redémarrer le backend.

### Service NLP indisponible

Vérifier que FastAPI est démarré sur le port 8000 et que l'adresse configurée dans `application.properties` est correcte.

### Page d'accueil introuvable

Vérifier que les fichiers HTML, CSS et JavaScript sont présents dans `src/main/resources/static/`.

Nettoyer et redémarrer le backend :

```powershell
.\mvnw.cmd clean compile

.\mvnw.cmd spring-boot:run
```

### Problème d'encodage

Enregistrer les fichiers sources en UTF-8 et configurer le terminal :

```powershell
chcp 65001

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
```

## 18. Documentation générale

Pour plus d'informations concernant les données, le prétraitement, l'architecture du modèle Deep Learning et son entraînement, consulter le [README principal du projet](../README.md).
