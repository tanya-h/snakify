package snakify;

import java.awt.*;
import java.awt.Point;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.ArrayList;


/**
 * This class contains GUI and logic for the Snake game.
 * @author tanya
 */

class Main extends JFrame{


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



    public static class SnakePanel extends JPanel implements ActionListener{

        //TODO instead - manage an array of snakes
        private ArrayList<Snake> snakes = new ArrayList<>();
        private ArrayList<Food> treats = new ArrayList<>();
        private HeroSnake hero;


        private final static int WIDTH = 800, HEIGHT = 640;
        private final static int SIDE = 32;
        public  final static int CELLSx = WIDTH/SIDE;
        public  final static int CELLSy = HEIGHT/SIDE;


        int DELAY = 200; //millis
        private Timer timer;


        public SnakePanel(){

            setPreferredSize(new Dimension(WIDTH+1, HEIGHT+1));

            //TODO add enemies
            //for (int i = 0; i < enemies; i++) snakes.add(new Snake());

            //add HeroSnake
            hero = new HeroSnake();
            snakes.add(hero);
            setFocusable(true);

            generateTreats();
            generateControls();
            timer = new Timer(DELAY, this); timer.start();
        }


        //tests
        private boolean gameOver(int x, int y) { return (hero.lives == 0 || x >= CELLSx || x < 0 || y >= CELLSy || y < 0); }
        private boolean inSnake(int x, int y) {return hero.points.contains(new Point(x,y));}
        //TODO test for collision with other future snakes or itself

        private void moveSnake(int x, int y){
            if (    !gameOver(x, y) &&
                    !inSnake(x,y)) {

                hero.points.add(new Point(x, y));             // adds new head to the end of the list
                hero.points.remove(hero.points.getFirst());
               //equivalent to points.remove(); removes tail
            } else {
                JOptionPane.showMessageDialog(SnakePanel.this,
                        "LOOOOSER!",
                        "GAME OVER", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            repaint();
        }

        private void feedSnake(int x, int y){
            for (Food food : treats) {
                if (food.x == x && food.y == y) {

                    hero.eat(food);
                    repaint();
                    speedify();
                    generateTreats();
                    return;
                }
            }
        }

        private void speedify(){
            if (DELAY == 25) return;
            if (hero.scores % 20 == 0) {
                DELAY -= 25;
                timer.setDelay(DELAY);
            }
        }


        @Override
        public void actionPerformed(ActionEvent e){
            //move the snake parts
            //so far only moves the hero
                Point head = hero.getHead();
                Point neck = hero.getNeck();

                int nx = head.x - (neck.x - head.x);
                int ny = head.y - (neck.y - head.y);

                feedSnake(nx, ny);
                moveSnake(nx, ny);

        }

        private void generateTreats(){
            treats.clear();

            for (int i = 0; i < SnakeRandomizer.randomTreat()+1; i ++) { //some random n treats
                Food food =   new Food(SnakeRandomizer.randomX(),
                                       SnakeRandomizer.randomY());

                while (hero.points.contains(food))
                        food = new Food(SnakeRandomizer.randomX(),
                                        SnakeRandomizer.randomY());
                treats.add(food);
            }

            //ensure at least 1 is Gourmet
            treats.get(0).setType(FoodType.GourmetFood);
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


        //only for hero
        private class ControlAction extends AbstractAction {

            private String key;
            ControlAction(String key) { this.key = key;}

            @Override
            public void actionPerformed(ActionEvent e) {

                Point head = hero.getHead(); //that's where our head it - in the tail.. this shit cray
                int x =     head.x;
                int y =     head.y;
                switch (key) {
                    case ("RIGHT"): x = x + 1;break;
                    case ("LEFT"):  x = x - 1;break;
                    case ("DOWN"):  y = y + 1;break;
                    case ("UP"):    y = y - 1;break;
                }
                //check if snake already faces this direction
                //if true, do nothing
                Point neck = hero.getNeck();
                int diffx = Math.max(x, neck.x) - Math.min(x, neck.x);
                int diffy = Math.max(y, neck.y) - Math.min(y, neck.y);
                int samex = x - neck.x;
                int samey = y - neck.y;
                if (x == neck.x && y == neck.y) return;
                if(diffx == 2 && samey == 0 || diffy == 2 && samex == 0) return;

                feedSnake(x,y);
                moveSnake(x, y);
            }
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            //background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.GRAY);
            int x =0, y = 0;
            while (x < WIDTH){
                // verticals bars
                g.drawLine(x, 0, x, HEIGHT);
                //horizontal bars
                g.drawLine(0, y, WIDTH, y);

                x += SIDE;
                y += SIDE;
            }

            //the snake
            for (Point p : hero.points) {
                g.fillRect(p.x * SIDE, p.y * SIDE, SIDE, SIDE);
            }
            //the snake's head
            g.setColor(Color.RED);
            g.fillRect(hero.getHead().x * SIDE, hero.getHead().y * SIDE, SIDE, SIDE);


            //explosion
            //TODO delay next frame to extend animation
            if (hero.exploding){
                g.drawImage(Images.explosion, hero.getHead().x *SIDE, hero.getHead().y * SIDE, null);
                hero.exploding = false;
            }

            //food
            for (Food food : treats) {
                //treats as balls
                //g.setColor(food.type.getColor());
                //g.fillRoundRect(food.x * SIDE, food.y * SIDE, SIDE, SIDE, 20, 25);

                //treats as images
                g.drawImage(food.type.getScaledImage(SIDE,SIDE),food.x * SIDE, food.y * SIDE, null);
            }


            //scores
            g.setFont(new Font("Courier", Font.PLAIN, 20));
            g.setColor(Color.MAGENTA);
            g.drawString("Calories: " + hero.scores, 10, 20);

            //lives
            g.setColor(Color.GREEN);
            g.drawString("Lives: " + hero.lives, 10, 45);
        }
    }
}
