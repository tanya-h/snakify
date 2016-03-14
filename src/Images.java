package snakify;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;


/**
 * This class initialises the images used in Snake.
 * @author tanya
 */
public class Images {

    static Image cupcake1, cupcake2, bug, dynamite, explosion;
    static ClassLoader img =  Images.class.getClassLoader();

    static{
        try {
            cupcake1 = ImageIO.read(img.getResourceAsStream("img/cupcake1.png"));
            cupcake2 = ImageIO.read(img.getResourceAsStream("img/cupcake2.png"));
            bug = ImageIO.read(img.getResourceAsStream("img/bug.png"));
            dynamite = ImageIO.read(img.getResourceAsStream("img/dynamite.png"));

            explosion = new ImageIcon(ImageIO.read(img.getResourceAsStream("img/explosion.gif"))).getImage();
        } catch (IOException ex){ ex.printStackTrace();}
    }
}
