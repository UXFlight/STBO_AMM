
# STBO_AMM_Project

## Aperçu

STBO_AMM_Project est une application Java développée dans le cadre du CPDLC Research Program. Elle simule et visualise des données aéronautiques en utilisant X-Plane, Ingescape, et JavaFX. Le projet charge des données d’aéroport depuis `apt.dat` (inclus pour KLAX) et permet d’interagir avec un agent AMM.

## Fonctionnalités

- **Connexion à X-Plane** : récupération des données de position, vitesse, etc.
- **Communication via Ingescape** : interaction avec l'agent Aircraft Movement Manager (AMM).
- **Visualisation graphique JavaFX** : affichage de l’aéroport KLAX.
- **Calculs géographiques avec jcoord**.

## Prérequis

- **Java** : Version 21 ou supérieure.
- **X-Plane** : doit être installé et lancé.
- **Ingescape** : serveur actif (ex : `ws://10.10.10.142:5925`).
- **JavaFX, apt.dat, librairies** : incluses dans le projet.

## Installation

1. Cloner le dépôt

```bash
git clone https://github.com/UXFlight/STBO_AMM_Project.git
cd STBO_AMM_Project
```

## Récupération des fichiers volumineux avec Git LFS

Le fichier `apt.dat` et le fichier `lib/libjfxwebkit.so` sont des fichiers volumineux nécessaires pour exécuter l'application. GitHub ayant une limite de taille de fichier de 100 Mo, ces fichiers sont gérés par **Git Large File Storage (Git LFS)**.

### Étapes pour configurer Git LFS

1. Installez [Git LFS](https://git-lfs.github.com/) si ce n'est pas déjà fait.

   - Sur **Linux** :
     ```bash
     sudo apt-get install git-lfs
     ```
   - Sur **macOS** (via Homebrew) :
     ```bash
     brew install git-lfs
     ```
   - Sur **Windows**, téléchargez et installez Git LFS à partir de [git-lfs.github.com](https://git-lfs.github.com/).

2. Clonez le dépôt et récupérez les fichiers volumineux avec la commande suivante :
   ```bash
   git lfs pull


## Exécution

### Lancer le projet

- **Linux/macOS** :
  Exécute le script `run.sh` pour compiler et lancer l’application.

```bash
./scripts/run.sh
```

- **Windows** :
  Double-clique sur `run.bat` ou exécute le fichier en ligne de commande :

```bash
./scripts/run.bat
```

Ces scripts se chargeront de compiler et de lancer automatiquement l'application.

## Scripts de gestion du projet

### 1. Nettoyage du projet

Le script de nettoyage supprime les répertoires de compilation et les fichiers temporaires. Cela permet de réinitialiser le projet avant de le recompiler.

- **Linux/macOS** :
  Exécute le script `clean.sh` pour compiler et lancer l’application.

```bash
./scripts/clean.sh
```

- **Windows** :
  Double-clique sur `clean.bat` ou exécute le fichier en ligne de commande :

```bash
./scripts/clean.bat
```

## Résultat attendu

- Interface JavaFX avec les pistes/taxiways de KLAX.
- Données simulées si X-Plane est actif.
- Agent AMM actif si Ingescape est connecté.

## Dépannage

- Vérifiez que `apt.dat` est à la racine du projet.
- Si Ingescape n'est pas connecté, l'application restera partiellement fonctionnelle.
- X-Plane doit être lancé pour activer XPConnect.

## Problèmes connus

- **Avertissement SLF4J** : Plusieurs bindings trouvés, mais sans impact.
- **Timeout Ingescape** : Si le serveur Ingescape n’est pas disponible, un timeout peut se produire.
