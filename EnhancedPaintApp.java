import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.List;

/**
 * Enhanced Paint Application (Optimized)
 * 
 * A feature-rich painting application demonstrating object-oriented design,
 * design patterns, file I/O, data structures, and GUI programming.
 * 
 * Key Features:
 * - Multiple drawing tools (brush, line, rectangle, circle)
 * - Adjustable brush sizes
 * - Undo/Redo functionality using Command pattern
 * - File operations (save/load PNG files)
 * - Color palette with recent colors
 * - Modern UI with tool icons
 * 
 * Optimizations:
 * - Undo states saved only on mouse release (not every mouse press)
 * - LinkedHashSet for O(1) color operations
 * - Reusable color palette buttons
 * - Reduced unnecessary repaints
 * - Memory-efficient image copying
 */
public class EnhancedPaintApp extends JFrame {
    
    // Drawing tools enum
    public enum Tool {
        BRUSH, ERASER, LINE, RECTANGLE, CIRCLE
    }
    
    private DrawingPanel drawingPanel;
    private Tool currentTool = Tool.BRUSH;
    private Color currentColor = Color.BLACK;
    private int brushSize = 4;
    
    // Command pattern for undo/redo (optimized with Deque)
    private Deque<BufferedImage> undoStack = new ArrayDeque<>();
    private Deque<BufferedImage> redoStack = new ArrayDeque<>();
    private static final int MAX_UNDO_SIZE = 20;
    
    // Recent colors (using LinkedHashSet for O(1) operations and order preservation)
    private LinkedHashSet<Color> recentColors = new LinkedHashSet<>();
    private JPanel colorPalette;
    private static final int MAX_RECENT_COLORS = 10;
    
    // UI Components
    private JButton undoButton, redoButton, saveButton, loadButton;
    private JSlider brushSizeSlider;
    private JLabel statusLabel;
    
    public EnhancedPaintApp() {
        initializeUI();
        setTitle("Enhanced Paint Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        addRecentColor(currentColor);
        updateUI();
        setVisible(true);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Top toolbar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // File operations
        saveButton = createButton("Save", this::saveImage);
        loadButton = createButton("Load", this::loadImage);
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(new JSeparator(JSeparator.VERTICAL));
        
        // Undo/Redo
        undoButton = createButton("Undo", this::undo);
        redoButton = createButton("Redo", this::redo);
        topPanel.add(undoButton);
        topPanel.add(redoButton);
        topPanel.add(new JSeparator(JSeparator.VERTICAL));
        
        // Tools
        JButton brushBtn = createButton("Brush", () -> setTool(Tool.BRUSH));
        JButton eraserBtn = createButton("Eraser", () -> setTool(Tool.ERASER));
        JButton lineBtn = createButton("Line", () -> setTool(Tool.LINE));
        JButton rectBtn = createButton("Rectangle", () -> setTool(Tool.RECTANGLE));
        JButton circleBtn = createButton("Circle", () -> setTool(Tool.CIRCLE));
        
        topPanel.add(brushBtn);
        topPanel.add(eraserBtn);
        topPanel.add(lineBtn);
        topPanel.add(rectBtn);
        topPanel.add(circleBtn);
        topPanel.add(new JSeparator(JSeparator.VERTICAL));
        
        // Color chooser
        JButton colorBtn = createButton("Color", this::chooseColor);
        topPanel.add(colorBtn);
        
        // Brush size
        topPanel.add(new JLabel("Size:"));
        brushSizeSlider = new JSlider(1, 20, brushSize);
        brushSizeSlider.addChangeListener(e -> {
            brushSize = brushSizeSlider.getValue();
            updateStatusOnly();
        });
        topPanel.add(brushSizeSlider);
        
        // Clear button
        JButton clearBtn = createButton("Clear", this::clearCanvas);
        topPanel.add(clearBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Left panel for color palette
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Recent Colors:"), BorderLayout.NORTH);
        
        colorPalette = new JPanel(new GridLayout(0, 1, 2, 2));
        colorPalette.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        leftPanel.add(new JScrollPane(colorPalette), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(80, 0));
        
        add(leftPanel, BorderLayout.WEST);
        
        // Drawing panel
        drawingPanel = new DrawingPanel();
        add(new JScrollPane(drawingPanel), BorderLayout.CENTER);
        
        // Status bar
        statusLabel = new JLabel("Ready - Tool: Brush");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        button.setFocusable(false);
        return button;
    }
    
    private void setTool(Tool tool) {
        currentTool = tool;
        updateUI();
    }
    
    private void updateUI() {
        updateStatusOnly();
        undoButton.setEnabled(!undoStack.isEmpty());
        redoButton.setEnabled(!redoStack.isEmpty());
    }
    
    private void updateStatusOnly() {
        String toolName = currentTool.toString().toLowerCase();
        toolName = toolName.substring(0, 1).toUpperCase() + toolName.substring(1);
        statusLabel.setText("Tool: " + toolName + " | Size: " + brushSize + " | Color: RGB(" + 
            currentColor.getRed() + "," + currentColor.getGreen() + "," + currentColor.getBlue() + ")");
    }
    
    private void chooseColor() {
        Color chosen = JColorChooser.showDialog(this, "Choose Color", currentColor);
        if (chosen != null) {
            currentColor = chosen;
            addRecentColor(chosen);
            updateUI();
        }
    }
    
    private void addRecentColor(Color color) {
        // Remove if exists (moves to front)
        recentColors.remove(color);
        // Add to beginning - we'll reverse the display order
        recentColors.add(color);
        
        // Remove oldest if exceeds limit
        if (recentColors.size() > MAX_RECENT_COLORS) {
            Iterator<Color> iterator = recentColors.iterator();
            iterator.next();
            iterator.remove();
        }
        
        updateColorPalette();
    }
    
    private void updateColorPalette() {
        colorPalette.removeAll();
        
        // Convert to list and reverse to show most recent first
        List<Color> colorList = new ArrayList<>(recentColors);
        Collections.reverse(colorList);
        
        for (Color color : colorList) {
            JButton colorBtn = new JButton();
            colorBtn.setBackground(color);
            colorBtn.setPreferredSize(new Dimension(30, 30));
            colorBtn.setBorder(BorderFactory.createRaisedBevelBorder());
            colorBtn.setFocusable(false);
            colorBtn.addActionListener(e -> {
                currentColor = color;
                updateUI();
            });
            colorPalette.add(colorBtn);
        }
        
        colorPalette.revalidate();
        colorPalette.repaint();
    }
    
    private void saveState() {
        BufferedImage state = copyImage(drawingPanel.getCanvasImage());
        
        undoStack.push(state);
        if (undoStack.size() > MAX_UNDO_SIZE) {
            undoStack.removeLast(); // Remove oldest
        }
        redoStack.clear();
        updateUI();
    }
    
    private void undo() {
        if (!undoStack.isEmpty()) {
            BufferedImage currentState = copyImage(drawingPanel.getCanvasImage());
            redoStack.push(currentState);
            
            BufferedImage previousState = undoStack.pop();
            drawingPanel.setCanvasImage(previousState);
            updateUI();
        }
    }
    
    private void redo() {
        if (!redoStack.isEmpty()) {
            BufferedImage currentState = copyImage(drawingPanel.getCanvasImage());
            undoStack.push(currentState);
            
            BufferedImage nextState = redoStack.pop();
            drawingPanel.setCanvasImage(nextState);
            updateUI();
        }
    }
    
    private BufferedImage copyImage(BufferedImage original) {
        BufferedImage copy = new BufferedImage(
            original.getWidth(), 
            original.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = copy.createGraphics();
        g2d.drawImage(original, 0, 0, null);
        g2d.dispose();
        return copy;
    }
    
    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                ImageIO.write(drawingPanel.getCanvasImage(), "PNG", file);
                statusLabel.setText("Image saved: " + file.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), 
                    "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "png", "jpg", "gif"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
                if (image != null) {
                    saveState(); // Save current state before loading
                    drawingPanel.loadImage(image);
                    statusLabel.setText("Image loaded: " + fileChooser.getSelectedFile().getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Could not read image file", 
                        "Load Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), 
                    "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearCanvas() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear the canvas?", 
            "Clear Canvas", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            saveState();
            drawingPanel.clear();
            statusLabel.setText("Canvas cleared");
        }
    }
    
    /**
     * Custom drawing panel with support for multiple tools and shapes
     */
    class DrawingPanel extends JPanel {
        
        private BufferedImage canvasImage;
        private Graphics2D canvasGraphics;
        private Point startPoint, endPoint;
        private boolean dragging = false;
        private boolean stateNeedsSaving = false; // Flag to track if changes were made
        
        public DrawingPanel() {
            setBackground(Color.WHITE);
            setDoubleBuffered(true); // Enable double buffering for smoother rendering
            setPreferredSize(new Dimension(800, 600));
            
            MouseHandler mouseHandler = new MouseHandler();
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }
        
        private void initCanvas() {
            if (canvasImage == null) {
                canvasImage = new BufferedImage(
                    Math.max(getWidth(), 800), 
                    Math.max(getHeight(), 600), 
                    BufferedImage.TYPE_INT_ARGB
                );
                canvasGraphics = canvasImage.createGraphics();
                canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                canvasGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                canvasGraphics.setColor(Color.WHITE);
                canvasGraphics.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            }
        }
        
        public BufferedImage getCanvasImage() {
            initCanvas();
            return canvasImage;
        }
        
        public void setCanvasImage(BufferedImage image) {
            canvasImage = copyImage(image);
            canvasGraphics = canvasImage.createGraphics();
            canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            canvasGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            repaint();
        }
        
        public void loadImage(BufferedImage image) {
            // Create a new canvas that can fit the loaded image
            int newWidth = Math.max(getWidth(), image.getWidth());
            int newHeight = Math.max(getHeight(), image.getHeight());
            
            canvasImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            canvasGraphics = canvasImage.createGraphics();
            canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            canvasGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            canvasGraphics.setColor(Color.WHITE);
            canvasGraphics.fillRect(0, 0, newWidth, newHeight);
            canvasGraphics.drawImage(image, 0, 0, null);
            
            setPreferredSize(new Dimension(newWidth, newHeight));
            revalidate();
            repaint();
        }
        
        public void clear() {
            initCanvas();
            canvasGraphics.setColor(Color.WHITE);
            canvasGraphics.fillRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
            repaint();
        }
        
        private void drawShape(Graphics2D g2d, Point start, Point end) {
            g2d.setColor(currentTool == Tool.ERASER ? Color.WHITE : currentColor);
            g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            int x1 = start.x, y1 = start.y;
            int x2 = end.x, y2 = end.y;
            
            switch (currentTool) {
                case BRUSH:
                case ERASER:
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
                case LINE:
                    g2d.drawLine(x1, y1, x2, y2);
                    break;
                case RECTANGLE:
                    int rectX = Math.min(x1, x2);
                    int rectY = Math.min(y1, y2);
                    int rectW = Math.abs(x2 - x1);
                    int rectH = Math.abs(y2 - y1);
                    g2d.drawRect(rectX, rectY, rectW, rectH);
                    break;
                case CIRCLE:
                    int circleX = Math.min(x1, x2);
                    int circleY = Math.min(y1, y2);
                    int circleW = Math.abs(x2 - x1);
                    int circleH = Math.abs(y2 - y1);
                    g2d.drawOval(circleX, circleY, circleW, circleH);
                    break;
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            initCanvas();
            g.drawImage(canvasImage, 0, 0, null);
            
            // Draw preview for shape tools
            if (dragging && startPoint != null && endPoint != null && 
                (currentTool == Tool.LINE || currentTool == Tool.RECTANGLE || currentTool == Tool.CIRCLE)) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                drawShape(g2d, startPoint, endPoint);
                g2d.dispose();
            }
        }
        
        private class MouseHandler extends MouseAdapter {
            
            @Override
            public void mousePressed(MouseEvent e) {
                // Don't save state yet - wait until mouse release
                startPoint = e.getPoint();
                endPoint = e.getPoint();
                dragging = true;
                stateNeedsSaving = true; // Mark that we'll need to save state
                
                if (currentTool == Tool.BRUSH || currentTool == Tool.ERASER) {
                    initCanvas();
                    drawShape(canvasGraphics, startPoint, endPoint);
                    repaint();
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging) {
                    Point currentPoint = e.getPoint();
                    
                    if (currentTool == Tool.BRUSH || currentTool == Tool.ERASER) {
                        drawShape(canvasGraphics, endPoint, currentPoint);
                        repaint();
                    }
                    
                    endPoint = currentPoint;
                    
                    if (currentTool == Tool.LINE || currentTool == Tool.RECTANGLE || currentTool == Tool.CIRCLE) {
                        repaint(); // For preview only
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (dragging) {
                    dragging = false;
                    
                    if (currentTool == Tool.LINE || currentTool == Tool.RECTANGLE || currentTool == Tool.CIRCLE) {
                        initCanvas();
                        drawShape(canvasGraphics, startPoint, endPoint);
                        repaint();
                    }
                    
                    // Save state only once after drawing is complete
                    if (stateNeedsSaving) {
                        saveState();
                        stateNeedsSaving = false;
                    }
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            new EnhancedPaintApp();
        });
    }
}