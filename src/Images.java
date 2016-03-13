package snakify;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


/**
 * This class initialises the images used in Snake.
 * @author tanya
 */
public class Images {

    static Image cupcake1, cupcake2, bug, dynamite, explosion;


    static{
        try {
            cupcake1 = ImageIO.read(Images.class.getResource("../img/cupcake1.png"));
            cupcake2 = ImageIO.read(Images.class.getResource("../img/cupcake2.png"));
            bug = ImageIO.read(Images.class.getResource("../img/bug.png"));
            dynamite = ImageIO.read(Images.class.getResource("../img/dynamite.png"));

            explosion = new ImageIcon(Images.class.getResource("../img/explosion.gif")).getImage();
           // explosion = ImageIO.read(Images.class.getResource("img/explosion.gif"));
        } catch (IOException ex){}
    }
}
