# ausbildungsnachweise
[![Autobuild and Release to DockerHub](https://github.com/Velyn-N/ausbildungsnachweise/actions/workflows/build.yml/badge.svg)](https://github.com/Velyn-N/ausbildungsnachweise/actions/workflows/build.yml)

<br>

Dieses Repo enthält ein Tool zum Führen, Verwalten und Exportieren von Ausbildungsnachweisen der IHK.<br>
Die Interaktion mit der Applikation erfolgt per WebInterface.

Das Projekt wurde in Java 17 ([Quarkus Framework](https://quarkus.io/)) und Vue.js 3 ([Quasar Framework](https://quasar.dev/)) geschrieben.

<br>

## Die Applikation installieren

### __Vorbereitung__
Installiere Docker auf deinem System. [Hier](https://docs.docker.com/engine/install/) findest du den offiziellen Guide.

### __Container-Einrichtung und -Konfiguration__
In diesem Guide werden die vorkompilierten Docker-Container von DockerHub benutzt. Um eigene Container einzusetzen ändere den Imagenamen.<br>
<br>
Weise einen Port deines Systems dem Port `:8080` des Containers zu, um später auf das WebInterface zugreifen zu können.<br>
- "1234" entspricht dabei dem Port auf welchem das WebInterface erreichbar sein wird.
- "/path/to/your/data" entspricht dem Pfad auf dem Hostsystem an dem die Dateien gespeichert werden sollen.
- "MeineApplikation" ist der Name unter dem der Container verfügbar sein wird.
```
docker run -d \
    -p 1234:8080 \
    -v /path/to/your/data:/app/nachweise/data \
    --name MeineApplikation \
    velyn/ausbildungsnachweise
```

### __Initiale Einrichtung der Applikation__
Dieser Teil wird hinzugefügt, sobald er implementiert wurde.
