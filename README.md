# 🕋 Ko Ja Match Up - Server

[![Kotlin](https://img.shields.io/badge/Kotlin-1.6.10-blue.svg?style=flat&logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-5AD6AB.svg?style=flat&logo=Gradle&logoColor=white)](http://kotlinlang.org)
[![Java-17](https://img.shields.io/badge/Java-17-red.svg?style=flat&logo=Java&logoColor=white)](https://www.oracle.com/technetwork/java/javase/12-relnote-issues-5211422.html)
[![Docker Hub](https://img.shields.io/badge/Docker_Hub-1.0.3-2496ed.svg?style=flat&logo=Docker&logoColor=white)](https://hub.docker.com/repository/docker/tobsef/kojamatchup-server)

Server application for the [KoJaMatchUp](https://github.com/TobseF/KoJaMatchUp) multiplayer match up game. It provides a
websocket connection on port `8080` which can be used by web or desktop clients. It broadcasts the game state and game
events, so all clients stay (hopefully) in sync.

🎮 Online demo: [MatchUp App](https://tobsef.github.io/KoJaMatchUp/)

### 🐳 Docker

The server is available at Docker Hub:  
[kojamatchup-server](https://hub.docker.com/repository/docker/tobsef/kojamatchup-server)  
You can build the image by your own:  
Run Gradle: `Server -> Tasks -> jib -> jibDockerBuild`

### Resources

Server is based on the [KorgeMultiplayerDemo](https://github.com/Kietyo/KorgeMultiplayerDemo)
from [Kietyo](https://github.com/Kietyo).