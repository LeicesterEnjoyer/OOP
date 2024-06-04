package oop.evolution.draw;

import oop.evolution.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class DrawWorld extends JFrame {
    public DrawWorld() {

        setTitle("Evolution");
        setSize(1000, 1020);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawPanel drawPanel = new DrawPanel();
        add(drawPanel);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawPanel.refreshGraphics();
            }
        });
        timer.start();
    }

    public void showWorld(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DrawWorld().setVisible(true);
            }
        });
    }

    class DrawPanel extends JPanel {
        private boolean isWhiteBackground = true;

        public void refreshGraphics() {
            isWhiteBackground = !isWhiteBackground;
            repaint();
        }

        private Color getColor(String hexColor){
            // Convert the hexadecimal color code to an integer
            int colorValue = Integer.parseInt(hexColor.substring(1), 16);

            // Extract the red, green, and blue components
            int red = (colorValue >> 16) & 0xFF;
            int green = (colorValue >> 8) & 0xFF;
            int blue = colorValue & 0xFF;

            // Create the Color object and return it
            return new Color(red, green, blue);
        }

        private void drawCreatures(Graphics2D g2d, int offsetI, int offsetJ, HashMap<String, HashMap<String, Integer>> creatures){
            int size = 16;
            int border = 2;

            // child plants
            int counter = 0;
            int tmpOffsetI = offsetI;

            // Draw child plants
            g2d.setColor(getColor("#bcab6c"));
            for(int i=0; i<creatures.get("PLANT").get("CHILD"); i++){
                g2d.fillOval(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }
            // Draw adult plants
            g2d.setColor(getColor("#525031"));
            for(int i=0; i<creatures.get("PLANT").get("ADULT"); i++){
                g2d.fillOval(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }
            // Draw child herbs
            g2d.setColor(getColor("#bcab6c"));
            for(int i=0; i<creatures.get("HERB").get("CHILD"); i++){
                g2d.fillRect(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }
            // Draw adult herbs
            g2d.setColor(getColor("#525031"));
            for(int i=0; i<creatures.get("HERB").get("ADULT"); i++){
                g2d.fillRect(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }
            // Draw child pred
            g2d.setColor(getColor("#c67e50"));
            for(int i=0; i<creatures.get("PRED").get("CHILD"); i++){
                g2d.fillRect(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }
            // Draw adult pred
            g2d.setColor(getColor("#753630"));
            for(int i=0; i<creatures.get("PRED").get("ADULT"); i++){
                g2d.fillRect(tmpOffsetI+border, offsetJ+border, size, size);
                tmpOffsetI += size+2*border;
                counter += 1;
                if (counter >= 4){
                    offsetJ += size+2*border;
                    tmpOffsetI = offsetI;
                    counter = 0;
                }
            }

        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(getColor("#c7a298"));

            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            // Set background color
            if(World.getInstance().getTime().isDay()){
                g2d.setColor(Color.WHITE);
            }
            else
                g2d.setColor(getColor("#d0d8c1"));

            int cellNumber = World.getProperty("BOARD_SIZE");
            int cellSize = (getWidth() - 50) / cellNumber;
            int offset = (getWidth() - cellSize*cellNumber) / 2;

            setForeground(Color.BLACK);
            int boardSize = cellNumber * cellSize;
            g2d.fillRect(offset, offset, boardSize, boardSize);

            HashMap<String, Integer> lastRain = World.getInstance().getLastRain();
            if(lastRain.get("AREA") != 0){
                g2d.setColor(getColor("#7e95a1"));
                g2d.fillRect((lastRain.get("X")*cellSize + offset), (lastRain.get("Y")*cellSize + offset), lastRain.get("AREA")*cellSize, lastRain.get("AREA")*cellSize);
                World.getInstance().setLastRain(0,0,0);
            }
            g2d.setColor(Color.BLACK);

            for(int i=cellSize + offset; i < boardSize; i += cellSize){
                g2d.drawLine(i, offset, i, boardSize + offset);
                g2d.drawLine(offset, i, boardSize + offset, i);
            }

            HashMap<String, HashMap<String, Integer>>[][] creatures = World.getInstance().getCreatures();
            for(int i=0; i<cellNumber; i++)
                for(int j=0; j<cellNumber; j++) {
                    drawCreatures(g2d, i * cellSize +offset, j * cellSize +offset, creatures[i][j]);
                }

        }
    }
}