
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

Le fichier `apt.dat` et le fichier `lib/libjfxwebkit.so` sont des fichiers volumineux nécessaires pour exécuter l'application. GitHub ayant une limite de taille de fichier de 100 Mo, ces fichiers sont compressé et gérer dans le script d'exécution.


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

# Structure
.
├── apt.dat
├── bin
│   ├── AgentCB
│   │   └── JavaAgentCB.class
│   ├── main
│   │   ├── Airport.class
│   │   ├── AptParser.class
│   │   ├── CoordConverter.class
│   │   ├── CPDLCManager$1.class
│   │   ├── CPDLCManager.class
│   │   ├── Main$1.class
│   │   ├── Main$2.class
│   │   ├── Main$3.class
│   │   ├── Main.class
│   │   ├── Runway.class
│   │   ├── Taxiway.class
│   │   ├── TaxiwayNode.class
│   │   ├── TaxiwaySegment.class
│   │   ├── Traffic.class
│   │   ├── Waypoint.class
│   │   ├── XPConnect$Instruction.class
│   │   ├── XPConnect$XPInstruction.class
│   │   ├── XPConnect.class
│   │   └── XPConnectTraffic.class
│   ├── uk
│   │   └── me
│   └── XPlaneConnect
│       ├── discovery
│       ├── ViewType.class
│       ├── WaypointOp.class
│       └── XPlaneConnect.class
├── classes
│   └── production
│       └── STBO_AMM_CPDLC
├── .classpath
├── .git
│   ├── branches
│   ├── config
│   ├── description
│   ├── FETCH_HEAD
│   ├── HEAD
│   ├── hooks
│   │   ├── applypatch-msg.sample
│   │   ├── commit-msg.sample
│   │   ├── fsmonitor-watchman.sample
│   │   ├── post-update.sample
│   │   ├── pre-applypatch.sample
│   │   ├── pre-commit.sample
│   │   ├── pre-merge-commit.sample
│   │   ├── prepare-commit-msg.sample
│   │   ├── pre-push.sample
│   │   ├── pre-rebase.sample
│   │   ├── pre-receive.sample
│   │   ├── push-to-checkout.sample
│   │   ├── sendemail-validate.sample
│   │   └── update.sample
│   ├── index
│   ├── info
│   │   └── exclude
│   ├── logs
│   │   ├── HEAD
│   │   └── refs
│   ├── objects
│   │   ├── info
│   │   └── pack
│   ├── packed-refs
│   └── refs
│       ├── heads
│       ├── remotes
│       └── tags
├── .gitignore
├── .idea
│   ├── .gitignore
│   ├── libraries
│   │   └── lib.xml
│   ├── misc.xml
│   ├── modules.xml
│   ├── vcs.xml
│   └── workspace.xml
├── ingescape
│   ├── .DS_Store
│   ├── jar
│   │   ├── .DS_Store
│   │   ├── ingescape.jar
│   │   └── ingescapeV2.jar
│   └── libs
│       ├── gson-2.8.6.jar
│       ├── kotlin-stdlib-1.4.0.jar
│       ├── logback-classic-1.2.3.jar
│       ├── logback-core-1.2.3.jar
│       ├── okhttp-4.9.0.jar
│       ├── okio-2.8.0.jar
│       └── slf4j-api-1.7.30.jar
├── lib
│   ├── javafx.base.jar
│   ├── javafx.controls.jar
│   ├── javafx.fxml.jar
│   ├── javafx.graphics.jar
│   ├── javafx.media.jar
│   ├── javafx.properties
│   ├── javafx.swing.jar
│   ├── javafx-swt.jar
│   ├── javafx.web.jar
│   ├── jinput-2.0.10.jar
│   ├── jna-jpms-5.16.0.jar
│   ├── jna-platform-jpms-5.16.0.jar
│   ├── libavplugin-54.so
│   ├── libavplugin-56.so
│   ├── libavplugin-57.so
│   ├── libavplugin-ffmpeg-56.so
│   ├── libavplugin-ffmpeg-57.so
│   ├── libavplugin-ffmpeg-58.so
│   ├── libavplugin-ffmpeg-59.so
│   ├── libavplugin-ffmpeg-60.so
│   ├── libavplugin-ffmpeg-61.so
│   ├── libdecora_sse.so
│   ├── libfxplugins.so
│   ├── libglassgtk3.so
│   ├── libglass.so
│   ├── libgstreamer-lite.so
│   ├── libjavafx_font_freetype.so
│   ├── libjavafx_font_pango.so
│   ├── libjavafx_font.so
│   ├── libjavafx_iio.so
│   ├── libjfxmedia.so
│   ├── libjfxwebkit.so
│   ├── libprism_common.so
│   ├── libprism_es2.so
│   └── libprism_sw.so
├── .project
├── README.md
├── run.bat
├── run.sh
├── src
│   ├── AgentCB
│   │   └── JavaAgentCB.java
│   ├── main
│   │   ├── Airport.java
│   │   ├── AptParser.java
│   │   ├── CoordConverter.java
│   │   ├── CPDLCManager.java
│   │   ├── Main.java
│   │   ├── Runway.java
│   │   ├── Taxiway.java
│   │   ├── TaxiwayNode.java
│   │   ├── TaxiwaySegment.java
│   │   ├── Traffic.java
│   │   ├── Waypoint.java
│   │   ├── XPConnect.java
│   │   └── XPConnectTraffic.java
│   ├── uk
│   │   └── me
│   └── XPlaneConnect
│       ├── discovery
│       ├── ViewType.java
│       ├── WaypointOp.java
│       └── XPlaneConnect.java
├── STBO_AMM_CPDLC.iml
└── structure.txt

37 directories, 123 files


## Dépannage

- Vérifiez que `apt.dat` est à la racine du projet.
- Si Ingescape n'est pas connecté, l'application restera partiellement fonctionnelle.
- X-Plane doit être lancé pour activer XPConnect.

## Problèmes connus

- **Avertissement SLF4J** : Plusieurs bindings trouvés, mais sans impact.
- **Timeout Ingescape** : Si le serveur Ingescape n’est pas disponible, un timeout peut se produire.
