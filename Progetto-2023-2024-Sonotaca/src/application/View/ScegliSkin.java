package application.View;

import application.Model.ImpostazioniDim;

import java.awt.*;
import java.util.ArrayList;

public class ScegliSkin {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] skinScreenFunctions;
    public Function[] getScreenFunctions() {
        return skinScreenFunctions;
    }

    public ScegliSkin(){
        getFunctions();
        this.skinScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
        functions.add(ImmaginiGioco.getSkinFuoco());
        functions.add(ImmaginiGioco.getSkinAcqua());
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point((ImpostazioniDim.Lunghezza_Finestra-MenuPrincipale.getStringWidth((String) func.getFirst(),GamePanel.Stile2))/2-292,62));
        items[1] = new Function(func.get(1), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getSkinFuoco().getWidth(null))/2-50,200));
        items[2] = new Function(func.get(2), new Point((ImpostazioniDim.Lunghezza_Finestra-ImmaginiGioco.getSkinAcqua().getWidth(null))/2+50,200));
        return items;
    }

    public Object select(Point mouseLocation) {
        for (Function item : skinScreenFunctions) {
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

