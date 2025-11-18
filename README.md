<h1 style="font-size: 40px; font-weight: 800; margin-bottom: 0;">🎨 PaintMSApp</h1>
<span style="font-size: 20px;">A single-file Java Swing drawing application demonstrating real UI engineering, Graphics2D rendering, and event-driven design.</span>

PaintMSApp is a fully interactive desktop drawing tool written entirely in one Java file. It showcases professional GUI development, rendering pipelines, event handling, and state management without relying on external libraries.

<h2 style="font-size: 34px; font-weight: 800;">🚀 Project Summary</h2>

PaintMSApp demonstrates how to organize input handling, drawing logic, undo behavior, and dynamic brush customization inside a clean single-file structure.
It highlights your understanding of rendering pipelines, Swing event listeners, and stateful UI programming.

<h2 style="font-size: 34px; font-weight: 800;">✨ Features</h2>

- Smooth freehand drawing with Graphics2D

- Adjustable brush size

- Custom color picker

- Eraser tool

- Clear canvas button

- Basic undo functionality

- Responsive, flicker-free Swing UI

<h2 style="font-size: 34px; font-weight: 800;">🧠 Engineering Highlights</h2>
<h3 style="font-size: 26px; font-weight: 700;">🎯 Event-Driven Architecture</h3>

- Built using Swing mouse listeners for stroke tracking

- Real-time rendering pipeline for smooth drawing


<h3 style="font-size: 26px; font-weight: 700;">🖌️ Graphics2D Rendering</h3>

- Anti-aliased strokes

- Smooth curves using continuous mouse sampling


<h3 style="font-size: 26px; font-weight: 700;">🧵 Internal Buffering</h3>

- Off-screen buffer prevents flickering

- Only redraws necessary segments


<h3 style="font-size: 26px; font-weight: 700;">📦 Centralized State Management</h3>

- Brush color, size, mode, and undo history in one unified manager

- Maintains single-file readability


<h2 style="font-size: 34px; font-weight: 800;">🛠️ Tech Used</h2>

- Java

- Swing / AWT

- Graphics2D

- Mouse event listeners

- Custom rendering loop

- Double buffering


<h2 style="font-size: 34px; font-weight: 800;">▶️ How to Run</h2>

Download or clone the file:
PaintMSApp.java

Compile:

javac PaintMSApp.java


Run:

java PaintMSApp

<h2 style="font-size: 34px; font-weight: 800;">🧩 Technical Challenges & Solutions</h2>
<h3 style="font-size: 26px; font-weight: 700;">🟦 1. Smooth Freehand Drawing</h3>

Challenge: Raw mouse events produce jagged, sharp lines.
Solution: Uses Graphics2D anti-aliasing + continuous stroke sampling for smooth, natural curves.

<h3 style="font-size: 26px; font-weight: 700;">🟩 2. Efficient Repainting</h3>

Challenge: Swing repaint cycles can flicker when drawing rapidly.
Solution: Uses an off-screen buffer + minimal repaint logic to eliminate flicker.

<h3 style="font-size: 26px; font-weight: 700;">🟨 3. Undo Stack in a Single File</h3>

Challenge: No multi-class architecture to organize states.
Solution: Stores canvas snapshots and restores them on demand — a simple but real undo mechanism.

<h3 style="font-size: 26px; font-weight: 700;">🟥 4. Tool Switching (Brush ↔ Eraser)</h3>

Challenge: Tools must work differently while sharing rendering logic.
Solution: Centralized flags manage tool mode cleanly without adding complexity.

<h2 style="font-size: 34px; font-weight: 800;">🌟 Future Enhancements</h2>

- Save canvas as PNG/JPG

- Redo support

- Shape tools (line, rectangle, oval)

- Brush presets

- Layers

- Image import

- Keyboard shortcuts

<h2 style="font-size: 34px; font-weight: 800;">👤 Author</h2>

Mahi Patel
Computer Science @ Toronto Metropolitan University
GitHub: github.com/mahip16
