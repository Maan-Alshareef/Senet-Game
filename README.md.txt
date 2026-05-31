`markdown
# 🎮 Ancient Egyptian Senet Game (Java Implementation)

An object-oriented Java implementation of **Senet**, one of the oldest known board games from ancient Egypt. This project features a fully functional game board, movement mechanics, traditional stick-throwing dice logic, and an **Expectiminimax AI** opponent for single-player mode.

---

## ✨ Features

* **Complete Senet Board Logic:** Full implementation of the 30-square grid board including special squares (House of Rebirth, House of Beauty, House of Water, etc.).
* **AI Opponent:** Integrated AI powered by the **Expectiminimax algorithm** to handle games with inherent randomness (dice/stick throws).
* **Traditional Mechanics:** Implements the authentic 4-stick throwing mechanism (`StickThrow`) to determine player moves.
* **Turn-Based Gameplay:** Supports structured turn switches between different `PlayerColor` sides.
* **Clean OOP Architecture:** Written strictly in Java with separated concerns for board state, pieces, movement rules, and game loop.

---

## 📂 Project Structure

The project consists of the following essential Java classes inside the `game` package:

* 🚀 **`SenetMain.java`** - The main entry point that initiates and handles the core game loop.
* 🧠 **`Expectiminimax.java`** - The AI brain utilizing probability and decision trees for computer moves.
* 🗺️ **`Board.java`** - Manages the grid, setup, and state of the 30 squares.
* 📦 **`Square.java` & `SquareType.java`** - Represents individual board cells and defines special hazards/safe zones.
* 🛡️ **`Piece.java` & `PlayerColor.java`** - Manages player tokens and identification.
* 👣 **`Move.java`** - Validates and processes piece movements across the board.
* 🎲 **`StickThrow.java`** - Simulates the ancient Egyptian 4-stick dice throwing mechanism.
