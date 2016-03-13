package snakify;

import java.awt.Point;
import java.util.LinkedList;


/**
 * The class hierarchy for Snake objects. Intended use of Snake class
 * is creating other snakes. HeroSnake is the main Snake, that can be
 * controlled in Main.
 * @author tanya
 */

class Snake {

    LinkedList<Point> points =  new LinkedList<>();

    //TODO howto randomize movement of other snakes???!

    public Point getHead(){ return points.get(points.size()-1);}
    public Point getNeck(){ return points.get(points.size()-2);}
}



class HeroSnake extends Snake {

    int scores;
    int lives;
    boolean exploding;


    HeroSnake(){

        points.add(new Point(0,8));     //head
        points.add(new Point(1,8));
        points.add(new Point(2,8));
        points.add(new Point(3,8));
        points.add(new Point(4,8));
        points.add(new Point(5,8));

        lives = 3;
        scores = 0;
    }


    public void eat(Food food){
        switch (food.type){
            case GourmetFood:       scores+=5; grow();          break; // Snake liked the treat
            case PoisonedFood:      scores-=5; shrink();        break;
            case ExplosiveFood:     lives-=1;  exploding = true;break;
            case VitalizingFood:    lives+=1;                   break;
        }
    }

    public void grow(){
        Point tail = points.peekFirst();
        points.addFirst(new Point(tail.x + 1, tail.y + 1));
    }

    public void shrink(){
        points.remove(points.getFirst());
    }
}