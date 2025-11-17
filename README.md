🎨 PaintMSApp

A single-file Java Swing drawing application demonstrating real UI engineering, event-driven design, Graphics2D rendering, and practical state management, all built without external libraries.

🚀 Project Summary

PaintMSApp is a fully interactive desktop drawing tool written in one Java file. It showcases GUI development, rendering pipelines, event handling, and application state management. The project demonstrates how to organize input handling, drawing logic, undo behavior, and brush customization within a clean and efficient structure.

✨ Features

- Smooth freehand drawing using Graphics2D
- Adjustable brush size
- Custom color picker
- Eraser tool
- Clear canvas option
- Basic undo support
- Responsive Swing UI

🧠 Engineering Highlights

- Event-driven architecture using Swing mouse listeners
- Anti-aliased Graphics2D rendering for smooth strokes
- Internal buffer-based repainting for flicker-free drawing
- Centralized state management for color, brush, tool mode, and undo history
- Clean and maintainable single-file structure that remains readable and extensible

🛠️ Tech Used

- Java
- Swing / AWT
- Graphics2D
- Mouse event listeners
- Custom rendering loop

▶️ How to Run

Download or clone the file:

PaintMSApp.java

Compile:

javac PaintMSApp.java

Run:

java PaintMSApp

🧩 Technical Challenges & Solutions

🟦 1. Smooth freehand drawing

Challenge: Raw mouse events draw jagged lines.

Solution: Uses Graphics2D with anti-aliasing and continuous stroke sampling to produce smooth curves.

🟩 2. Efficient repainting

Challenge: Swing repaints can flicker if mismanaged.

Solution: Maintains an off-screen buffer and redraws only the necessary portions during strokes.

🟨 3. Undo stack in a single-file structure

Challenge: No complex class structure.

Solution: Stores snapshots/states and restores them on command. Simple, but real and effective state control.

🟥 4. Tool switching (brush ↔ eraser)

Challenge: Tools must behave differently but share logic.

Solution: Centralized state flags handle tool decisions cleanly without overcomplication.

🌟 Future Enhancements

- Save canvas as PNG/JPG

- Redo support

- Shape tools (line, rectangle, oval)

- Brush presets

- Layers system

- Image import

- Keyboard shortcuts
