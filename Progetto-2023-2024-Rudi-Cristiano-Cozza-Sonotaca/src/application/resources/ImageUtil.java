package application.resources;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static Image flipImageHorizontally(Image image) {
        // Supponiamo che l'immagine sia già stata caricata
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        // Crea l'immagine che conterrà il risultato utilizzando BufferedImage
        BufferedImage flippedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        // Ottiene un Graphics2D da BufferedImage
        Graphics2D g2d = flippedImage.createGraphics();

        // Crea un AffineTransform per il capovolgimento orizzontale
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-imageWidth, 0);

        // Applica la trasformazione durante il disegno dell'immagine in BufferedImage
        g2d.drawImage(image, tx, null);

        // Pulisce le risorse
        g2d.dispose();

        // Restituisce l'immagine capovolta
        return flippedImage;
    }
}
