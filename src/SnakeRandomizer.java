package snakify;

import java.util.Random;

/**
 * This class provides facility to generate random numbers for the game
 * using constrains of Main class.
 * @author tanya
 */

public class SnakeRandomizer {

    public static int randomX(){
        return new Random().nextInt(Main.SnakePanel.CELLSx);
    }
    public static int randomY(){
        return new Random().nextInt(Main.SnakePanel.CELLSy);
    }
    public static int randomTreat(){return new Random().nextInt(FoodType.values().length); //choose random treat}
}
