package application.View;

import application.Model.ImpostazioniDim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Aiuto {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] helpScreenFunctions;
    public Function[] getScreenFunctions() {
        return helpScreenFunctions;
    }
    public Aiuto(){
        getFunctions();
        this.helpScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
        functions.add(ImmaginiGioco.getFreccia(1));
        functions.add(ImmaginiGioco.getFreccia(0));
    }

    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) func.getFirst(),GamePanel.Stile2))/2-292,62));
        items[1] = new Function(func.get(1), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getFreccia(1).getWidth(null))/2+300,580));
        items[2] = new Function(func.get(2), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getFreccia(0).getWidth(null))/2-100,580));
        return items;
    }
    public static int getStringWidth(String text, Font font) {
        JLabel label = new JLabel();
        FontMetrics fontMetrics = label.getFontMetrics(font);
        return fontMetrics.stringWidth(text);
    }
    public Object select(Point mouseLocation) {
        for (Function item : helpScreenFunctions) {
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY;
            if(item.getObject() instanceof String) inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            else inY = location.y <= mouseLocation.y && location.y + dimension.height >= mouseLocation.y;
            if (inX && inY) {
                return item.getObject();
            }
        }
        return null;
    }
}
