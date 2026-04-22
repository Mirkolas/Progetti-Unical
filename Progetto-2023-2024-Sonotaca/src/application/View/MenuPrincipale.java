package application.View;

import application.Model.ImpostazioniDim;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MenuPrincipale {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] mainScreenFunctions;

    public Function[] getScreenFunctions() {
        return mainScreenFunctions;
    }

    public MenuPrincipale(){
        getFunctions();
        this.mainScreenFunctions=createItems(this.functions);
    }
    private void getFunctions(){
        functions.add("Gioca");
        functions.add("Scegli skin");
        functions.add("Aiuto");
        functions.add("Esci");
        functions.add(ImmaginiGioco.getRiconoscimenti());
        functions.add("Reset  Progressi");
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(0),GamePanel.Stile2))/2, 377));
        items[1] = new Function(func.get(1), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(1),GamePanel.Stile2))/2,430));
        items[2] = new Function(func.get(2), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(2),GamePanel.Stile2))/2,477));
        items[3] = new Function(func.get(3), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(3),GamePanel.Stile2))/2,530));
        items[4] = new Function(func.get(4), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getRiconoscimenti().getWidth(null))/2+170,430));
        items[5] = new Function(func.get(5), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.get(5),GamePanel.Stile2))/2,580));
        return items;
    }

    public static int getStringWidth(String text, Font font) {
        JLabel label = new JLabel();
        FontMetrics fontMetrics = label.getFontMetrics(font);
        return fontMetrics.stringWidth(text);
    }

    public Object select(Point mouseLocation) {
        for(Function item : mainScreenFunctions) {
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX= location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
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