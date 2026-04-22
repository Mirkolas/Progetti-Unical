package application.view;

import application.model.Settings;
import java.awt.*;
import java.util.ArrayList;

public class MapScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] mapScreenFunctions;
    public Function[] getScreenFunctions() {
        return mapScreenFunctions;
    }
    public MapScreen(){
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
        items[0] = new Function(func.getFirst(), new Point(100,100));
        items[1] = new Function(func.get(1), new Point(500,100));
        items[2] = new Function(func.get(2), new Point((Settings.WINDOW_SIZE_X-300)/2,350));
        items[3] = new Function(func.getLast(), new Point(47,62));
        return items;
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


