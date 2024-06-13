# Deck of Cards Game

## Overview
This project is an implementation of deck of poker-style playing cards, along with a REST API that allows for a basic game operations.

## Features
- Create and delete a game
- Create a deck
- Add a deck to the game (Once added, the deck cannot be removed)
- Add/Remove players to the game
- Deal cards to players in a game from the game deck
- Get the list of cards for a player
- Get the list of players in a game along with total added value of all the cards player holds.
- Get count of how many cards per suit are left undealt in the game deck
- Event based logging for all the operations

## Setup
1. Clone the repository.
2. Navigate to the project directory.
3. Run the application using your preferred IDE or the command line with `./mvnw spring-boot:run`.

## Usage
- The REST API endpoints are available at `http://localhost:8080/`.
- Use tools like Postman to interact with the API.

## To-Do
- [ ] Implement basic game operations
- [ ] Add concurrency support
- [ ] Implement event logging
- [ ] Write unit tests
- [ ] Document the API endpoints