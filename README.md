# ausbildungsnachweise
[![Autobuild and Release to DockerHub](https://github.com/Velyn-N/ausbildungsnachweise/actions/workflows/build.yml/badge.svg)](https://github.com/Velyn-N/ausbildungsnachweise/actions/workflows/build.yml)

<br>

Dieses Repo enthält ein Tool zum Führen, Verwalten und Exportieren von Ausbildungsnachweisen der IHK.<br>
Die Interaktion mit der Applikation erfolgt per WebInterface.

Das Projekt wurde in Java 17 ([Quarkus Framework](https://quarkus.io/)) und Vue.js 3 ([Quasar Framework](https://quasar.dev/)) geschrieben.

<br>

## Die Applikation installieren

### __Vorbereitung__
Installiere [Docker](https://docs.docker.com/engine/install/) auf deinem System

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
Nach der Installation muss sichergestellt werden, dass die benötigten Dateien verfügbar sind. <br>
Die Lognachrichten beim Applikationsstart melden relevante Fehler. Die Applikation kann ohne diese Dateien nicht funktionieren.

Zum Anlegen neuer Benutzer navigiere zu der Seite `/#/new-user`.

<br>

## Entwicklung
Zum Entwickeln werden folgende Tools benötigt:
- Java 17 oder neuer
- NodeJS 16 oder neuer

Zuerst muss man die Dependencies des FrontEnds mit folgendem Befehl im Ordner `ausbildungsnachweise-quasar` installieren:
```shell
npm install
```

Anschließend startet man das BackEnd mit folgendem Befehl im Ordner `ausbildungsnachweise-quarkus`:
```shell
./mvnw compile quarkus:dev
```

Dann startet man das FrontEnd mit folgendem Befehl im Ordner `ausbildungsnachweise-quasar`:
```shell
npx dev
```

<br>

## Bauen für Production

1. Vue-App bauen:
```shell
npx quasar build
```

2. Dateien aus `ausbildungsnachweise-quasar/dist/spa` nach `ausbildungsnachweise-quarkus/src/main/resources/META-INF/resources` kopieren

3. Quarkus Applikation bauen:
```shell
./mvnw clean package -Dquarkus.package.type=uber-jar
```

4. (Optional) Docker Image bauen:
Dazu das Dockerfile `ausbildungsnachweise-quarkus/src/main/docker/Dockerfile.jvm` bauen.
