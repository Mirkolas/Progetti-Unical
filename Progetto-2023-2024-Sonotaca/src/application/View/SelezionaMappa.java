package application.View;

import application.Model.ImpostazioniDim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SelezionaMappa {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] mapScreenFunctions;
    public Function[] getScreenFunctions() {
        return mapScreenFunctions;
    }
    public SelezionaMappa(){
        getFunctions();
        this.mapScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add(ImmaginiGioco.getMap1(false));
        functions.add(ImmaginiGioco.getMap2(GamePanel.getLock2()));
        functions.add(ImmaginiGioco.getMap3(GamePanel.getLock3()));
        functions.add("Indietro");

    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getMap1(false).getWidth(null))/2,100));
        items[1] = new Function(func.get(1), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getMap2(true).getWidth(null))/2,350));
        items[2] = new Function(func.get(2), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getMap3(true).getWidth(null))/2,600));
        items[3] = new Function(func.get(3), new Point((ImpostazioniDim.Lunghezza_Finestra-getStringWidth((String) func.get(3),GamePanel.Stile2))/2-292,62));
        return items;
    }
    public static int getStringWidth(String text, Font font) {
        JLabel label = new JLabel();
        FontMetrics fontMetrics = label.getFontMetrics(font);
        return fontMetrics.stringWidth(text);
    }
    public Object select(Point mouseLocation) {
        for (Function item : mapScreenFunctions) {
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY;
            if(item.getObject() instanceof String) inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            else inY = location.y <= mouseLocation.y && location.y + dimension.height >= mouseLocation.y;
            if(inX && inY){
                return item.getObject();
            }
        }
        return null;
    }
}


