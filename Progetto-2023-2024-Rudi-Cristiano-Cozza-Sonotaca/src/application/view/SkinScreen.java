package application.view;

import java.awt.*;
import java.util.ArrayList;

public class SkinScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] skinScreenFunctions;
    public Function[] getScreenFunctions() {
        return skinScreenFunctions;
    }

    public SkinScreen(){
        getFunctions();
        this.skinScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
        functions.add(ImmaginiGioco.getSteve());
        functions.add(ImmaginiGioco.getAlex());
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(47,62));
        items[1] = new Function(func.get(1), new Point(370,505));
        items[2] = new Function(func.get(2), new Point(470,505));
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

