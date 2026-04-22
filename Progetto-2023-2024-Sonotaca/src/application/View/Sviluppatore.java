package application.View;

import java.awt.*;
import java.util.ArrayList;

public class Sviluppatore {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] aboutScreenFunctions;
    public Function[] getScreenFunctions() {
        return aboutScreenFunctions;
    }

    public Sviluppatore(){
        getFunctions();
        this.aboutScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
    }

    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(47,62));
        return items;
    }

    public Object select(Point mouseLocation) {
        for (Function item : aboutScreenFunctions){
            Dimension dimension = item.getDimension();
            Point location = item.getLocation();
            boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
            boolean inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;
            if (inX && inY) {
                return item.getObject();
            }
        }
        return null;
    }}
