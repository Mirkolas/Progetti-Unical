package application.view;

import java.awt.*;

public class Function {
    private final Object name;
    private Point location;
    private Dimension dimension;

    public Function(Object name, Point location){
        this.location = location;
        this.name = name;
        this.dimension = new Dimension();
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Point getLocation() {
        return location;
    }

    public Object getObject() {
        return name;
    }

    public void setDimension(Dimension dimension) {
        this.dimension=dimension;
    }

    public void setLocation(Point point) {
        this.location=point;
    }
}
