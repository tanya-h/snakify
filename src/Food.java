package snakify;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;


/**
 * These classes allow generating a random Food type.
 * There are 4 food types for the snake: Gourmet for growing,
 * Poisoned for shrinking, Explosive food takes 1 Snake life,
 * and finally Vitalising food brings it 1 life.
 *
 * Each food type is represented by an image and color tag (for testing).
 * @author tanya
 */
enum FoodType{

    GourmetFood(Color.magenta, Images.cupcake1),
    PoisonedFood(Color.blue, Images.bug),
    VitalizingFood(Color.green, Images.cupcake2),
    ExplosiveFood(Color.black, Images.dynamite);

    private Color color;
    private Image image;

    FoodType(Color col, Image img) {color = col; image = img;}
    public Color getColor(){return color;}
    public Image getImage(){return image;}
    public Image getScaledImage(int width, int height){return image.getScaledInstance(width, height, Image.SCALE_DEFAULT);}
}



public class Food extends Point {

    FoodType type;

    Food(int x, int y){
        super(x,y);
        type = FoodType.values()[SnakeRandomizer.randomTreat()];
    }

    public void setType(FoodType type) {
        this.type = type;
    }
}
