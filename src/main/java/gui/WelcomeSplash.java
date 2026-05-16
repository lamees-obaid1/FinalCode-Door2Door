package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class WelcomeSplash extends JFrame {

    private final String username;
    private final String role;

    public WelcomeSplash(String username, String role) {
        this.username = username;
        this.role = role;

        setTitle("Welcome");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);

        add(new SplashPanel());

        Timer closeTimer = new Timer(5500, e -> {
            new Dashboard(username, role).setVisible(true);
            dispose();
        });

        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    class SplashPanel extends JPanel {

        private final ArrayList<Bubble> bubbles = new ArrayList<>();
        private final Random random = new Random();

        private final Color bgColor = new Color(214, 156, 148);
        private final Color darkText = new Color(58, 32, 42);

        SplashPanel() {
            setBackground(bgColor);

            for (int i = 0; i < 40; i++) {
                bubbles.add(new Bubble());
            }

            Timer animationTimer = new Timer(25, e -> {
                for (Bubble b : bubbles) {
                    b.move();
                }
                repaint();
            });

            animationTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            for (Bubble b : bubbles) {
                g2.setColor(b.color);
                g2.fillOval(b.x, b.y, b.size, b.size);

                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, b.iconSize));
                g2.setColor(new Color(58, 32, 42, 170));
                g2.drawString(b.icon, b.x + 5, b.y + b.size - 8);
            }

            g2.setFont(new Font("SansSerif", Font.BOLD, 36));
            g2.setColor(darkText);
            drawCentered(g2, "Door2Door Company", 155);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            drawCentered(g2, "Loading your dashboard...", 390);

            drawLoadingLine(g2);

            g2.dispose();
        }

        private void drawCentered(Graphics2D g2, String text, int y) {
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            g2.drawString(text, x, y);
        }

        private void drawLoadingLine(Graphics2D g2) {
            int x = 170;
            int y = 410;
            int width = 360;
            int height = 10;

            g2.setColor(new Color(235, 185, 175, 170));
            g2.fillRoundRect(x, y, width, height, 20, 20);

            long time = System.currentTimeMillis();
            int movingWidth = (int) ((time / 10) % width);

            g2.setColor(new Color(160, 92, 148));
            g2.fillRoundRect(x, y, movingWidth, height, 20, 20);
        }

        class Bubble {

            int x;
            int y;
            int size;
            int speed;
            int iconSize;

            String icon;
            Color color;

            Bubble() {
                reset();
                y = random.nextInt(450);
            }

            void reset() {
                size = 30 + random.nextInt(40);
                x = random.nextInt(700);
                y = 450 + random.nextInt(200);
                speed = 1 + random.nextInt(3);
                iconSize = Math.max(18, size - 16);

                String[] icons = {
                        "📦", "🚚", "🛵", "📍", "🛒"
                };

                icon = icons[random.nextInt(icons.length)];

                Color[] colors = {
                        new Color(255, 230, 220, 95),
                        new Color(255, 210, 225, 90),
                        new Color(235, 205, 245, 90),
                        new Color(255, 235, 190, 85),
                        new Color(240, 190, 180, 90),
                        new Color(255, 245, 230, 80)
                };

                color = colors[random.nextInt(colors.length)];
            }

            void move() {
                y -= speed;

                if (y + size < 0) {
                    reset();
                }
            }
        }
    }
}