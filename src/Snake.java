package snakify;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import javax.swing.*;

import java.util.LinkedList;


class Snake extends JFrame{


    public static void main(String [] args){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frm = new JFrame("The Snake Game");
                frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frm.add(new SnakePanel(), BorderLayout.CENTER);
                frm.pack();
                frm.setLocationRelativeTo(null);
                frm.setVisible(true);
            }
        });
    }


    private static class SnakePanel extends JPanel implements ActionListener{

        private LinkedList<Point> points = new LinkedList<>();
        Point food;
        private final int CELLS = 10;
        private final int SIDE = 60;

        private static int scores;

        int DELAY = 450; //millis
        private Timer timer;


        public SnakePanel(){

            setPreferredSize(new Dimension(601, 601));
            points.add(new Point(0,5));     //head
            points.add(new Point(1,5));
            points.add(new Point(2,5));
            points.add(new Point(3,5));
            points.add(new Point(4,5));
            points.add(new Point(5,5));
            setFocusable(true);

            generateFood();
            generateControls();
            timer = new Timer(DELAY, this); timer.start();
        }


        //tests
        private boolean gameOver(int x, int y) { return (x >= CELLS || x < 0 || y >= CELLS || y < 0); }
        private boolean inSnake(int x, int y) {return points.contains(new Point(x,y));}

        private void moveSnake(int x, int y){
            if (    !gameOver(x, y) &&
                    !inSnake(x,y)) {
                points.add(new Point(x,y));             // adds new head to the end of the list
                points.remove(points.getFirst());      //equivalent to points.remove(); removes tail
            } else {
                JOptionPane.showMessageDialog(SnakePanel.this,
                        "LOOOOSER!",
                        "GAME OVER", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            repaint();
        }

        private void growSnake(int x, int y){
            if (food.x == x && food.y ==y){
                Point tail = points.peekFirst();
                points.addFirst(new Point(tail.x+1, tail.y+1));
                scores+=5;
                generateFood();
                repaint();
            }
        }


        @Override
        public void actionPerformed(ActionEvent e){
            //move the snake parts
            Point head = points.get(points.size()-1); //last
            Point neck = points.get(points.size()-2); //one but last

            int nx = head.x - (neck.x-head.x);
            int ny = head.y - (neck.y-head.y);

            growSnake(nx, ny);
            moveSnake(nx, ny);
        }

        private void generateFood(){
            food = new Point((int) (Math.random() * 10),
                             (int) (Math.random() * 10));
            if(points.contains(food)) generateFood();
        }

        private void generateControls(){
            InputMap imap = getInputMap(JPanel.WHEN_FOCUSED);
            ActionMap amap = getActionMap();
            imap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
            imap.put(KeyStroke.getKeyStroke("LEFT"), "left");
            imap.put(KeyStroke.getKeyStroke("DOWN"), "down");
            imap.put(KeyStroke.getKeyStroke("UP"), "up");

            amap.put("right", new ControlAction("RIGHT"));
            amap.put("left", new ControlAction("LEFT"));
            amap.put("down", new ControlAction("DOWN"));
            amap.put("up", new ControlAction("UP"));
        }


        private class ControlAction extends AbstractAction {

            private String key;
            ControlAction(String key) { this.key = key;}

            @Override
            public void actionPerformed(ActionEvent e) {

                Point head = points.peekLast(); //that's where our head it - in the tail.. this shit cray
                int x =     head.x;
                int y =     head.y;
                switch (key) {
                    case ("RIGHT"): x = x + 1;break;
                    case ("LEFT"):  x = x - 1;break;
                    case ("DOWN"):  y = y + 1;break;
                    case ("UP"):    y = y - 1;break;
                }
                growSnake(x,y);
                moveSnake(x, y);
            }
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 601, 601);

            g.setColor(Color.GRAY);
            int x =0, y = 0;
            while (x < 600){
                // verticals bars
                g.drawLine(x, 0, x, 600);
                //horizontal bars
                g.drawLine(0, y, 600, y);

                x += SIDE;
                y += SIDE;
            }
            //the snake
            for (Point p : points){
                g.fillRect(p.x*SIDE , p.y*SIDE, SIDE, SIDE);
            }

            //the snake's head
            g.setColor(Color.RED);
            g.fillRect(points.peekLast().x*SIDE , points.peekLast().y*SIDE, SIDE, SIDE);

            //food
            g.setColor(Color.MAGENTA);
            g.fillRoundRect(food.x*SIDE, food.y*SIDE, SIDE, SIDE, 20, 25);

            //scores
            g.setColor(Color.BLUE);
            g.setFont(new Font("Courier", Font.PLAIN, 20));
            g.drawString("Calories: " + scores, 10, 20);
        }

    }

}
