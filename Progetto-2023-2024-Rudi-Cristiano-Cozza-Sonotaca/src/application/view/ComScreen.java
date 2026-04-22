package application.view;
import java.awt.*;
import java.util.ArrayList;

public class ComScreen {
    private final ArrayList<Object> functions=new ArrayList<>();
    private final Function[] comScreenFunctions;
    public Function[] getScreenFunctions() {
        return comScreenFunctions;
    }
    public ComScreen(){
        getFunctions();
        this.comScreenFunctions=createItems(this.functions);
    }
    private void getFunctions() {
        functions.add("Indietro");
        functions.add(ImmaginiGioco.getWASD());
        functions.add(ImmaginiGioco.getFreccette());
    }
    private Function[] createItems(ArrayList<Object> func){
        if(func == null)
            return null;
        Function[] items = new Function[func.size()];
        items[0] = new Function(func.getFirst(), new Point(47,62));
        items[1] = new Function(func.get(1), new Point(170,160));
        items[2] = new Function(func.getLast(), new Point(520,160));
        return items;
    }

    public Object select(Point mouseLocation) {
        for (Function item : comScreenFunctions) {
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
