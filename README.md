<h1 align="center">Hangman Game</h1>

<p align="center">
  <a href="#overview">Overview</a> •
  <a href="#features">Features</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#instalation">Instalation</a> •
  <a href="#license">License</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  <img src="https://img.shields.io/badge/Author-SmartMatt-blue" />
</p>

## Overview

This repository hosts a **Java** application designed for playing the classic game of Hangman. The program features two GUI versions: a **web-based** interface and a **client-side** application using JavaFX. It incorporates several enhancements to enrich and facilitate gameplay.

## Features

- **Classic Hangman Gameplay**: Both versions offer the traditional Hangman experience, including letter guessing, accumulating failures, and replaying games.
- **Word Data Sources**: Retrieves word data from various sources, differing between implementations.
- **Scoreboard System**: A scoring system is present in both versions to showcase the best player attempts.

### Client Version
- **Word Database Upload**: Allows uploading word databases from .txt files.

### Web Version
- **Local Scoreboard**: Utilizes cookies for a local scoreboard.
- **Session Data Storage**: Facilitates separate gameplay on different browsers and enables returning to unfinished games.
- **Gameplay Record**: Last games are saved in the Payara database.

## Screenshots

<figure>
  <img src="https://smartmatt.pl/github/hangman-game/hangman-game-presentation.png" title="Presentation of both versions" width=75%>
  <figcaption>Presentation of both versions.</figcaption>
</figure>

<figure>
  <img src="https://smartmatt.pl/github/hangman-game/hangman-web-adding-words.png" title="Manually adding extra words to the Payara database in the web version." width=75%>
  <figcaption>Manually adding extra words to the Payara database in the web version.</figcaption>
</figure>

<figure>
  <img src="https://smartmatt.pl/github/hangman-game/hangman-client-creating-player.png" title="Creating the player" width=75%>
  <figcaption>The process of creating a user in the client version of the application.</figcaption>
</figure>


## Instalation

### Prerequisites
- Java JDK (version 11 or higher)
- Any Java IDE (e.g., IntelliJ IDEA, Eclipse)
- (For web version) Payara Server or any compatible server for running Java EE applications

### Running the Client Application
1. Clone the repository to your local machine.
2. Open the project in your Java IDE.
3. Locate the main class for the JavaFX application (`Main.java` or similar).
4. Run the `Main.java` file to start the client application.

### Setting up the Web Server
1. Ensure Payara Server or your chosen Java EE server is installed and configured.
2. Build the web application using Maven or your IDE’s build tool.
3. Deploy the `.war` file to the server:
   - For Payara, copy the `.war` file to the `payara5/glassfish/domains/domain1/autodeploy/` directory.
   - Alternatively, use the admin console to deploy the application manually.

### Accessing the Web Application
- After deploying, open a web browser.
- Navigate to `http://localhost:8080/[YourAppName]` (replace `[YourAppName]` with the name of your deployed application).
- The Hangman game should now be accessible via the web interface.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
&copy; 2023 Mateusz Płonka (SmartMatt). All rights reserved.
<a href="https://smartmatt.pl/">
    <img src="https://smartmatt.pl/github/smartmatt-logo.png" title="SmartMatt Logo" align="right" width="60" />
</a>

<p align="left">
  <a href="https://smartmatt.pl/">Portfolio</a> •
  <a href="https://github.com/SmartMaatt">GitHub</a> •
  <a href="https://www.linkedin.com/in/mateusz-p%C5%82onka-328a48214/">LinkedIn</a> •
  <a href="https://www.youtube.com/user/SmartHDesigner">YouTube</a> •
  <a href="https://www.tiktok.com/@smartmaatt">TikTok</a>
</p>
