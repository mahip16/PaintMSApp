🎨 PaintMSApp

A single-file Java Swing drawing application demonstrating real UI engineering, event-driven design, Graphics2D rendering, and practical state management — all built without external libraries.

🚀 Project Summary

PaintMSApp is a fully interactive desktop drawing tool written in one Java file. It showcases mastery of GUI development, rendering pipelines, event handling, and application state management. The project demonstrates how to organize input handling, drawing logic, undo behavior, and brush customization within a clean and efficient structure.

✨ Features

Smooth freehand drawing using Graphics2D

Adjustable brush size

Custom color picker

Eraser tool

Clear canvas option

Basic undo support

Responsive Swing UI

Entire application implemented in a single file

🧠 Engineering Highlights

Event-driven architecture using Swing mouse listeners

Anti-aliased Graphics2D rendering for smooth strokes

Internal buffer-based repainting for flicker-free drawing

Centralized state management for color, brush, tool mode, and undo history

Clean and maintainable single-file structure that remains readable and extensible

🛠️ Tech Used

Java

Swing / AWT

Graphics2D

Mouse event listeners

Custom rendering loop

▶️ How to Run

Download or clone the file:
PaintMSApp.java

Compile:
javac PaintMSApp.java

Run:
java PaintMSApp

🧩 Technical Challenges & Solutions

Implemented smooth freehand drawing through anti-aliasing and continuous stroke sampling

Managed repaint cycles efficiently to prevent flicker

Implemented undo functionality through internal state snapshots

Designed simple but effective tool switching for brush and eraser modes

🌟 Future Enhancements

Save canvas as PNG/JPG

Redo support

Shape tools (line, rectangle, oval)

Brush presets

Layers system

Image import

Keyboard shortcuts
