package hr.fer.zemris.irg.zad2;

import hr.fer.zemris.irg.factory.ColorFactory;
import hr.fer.zemris.irg.models.Triangle;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class Zad2SceneModel {
    @Getter
    private Color selectedColor;
    @Getter
    private List<Triangle> triangles;
    @Getter
    private List<Point> points;
    private List<Color> availableColors;
    private int currentColorIndex;
    @Getter
    @Setter
    private Point mousePointer;

    public Zad2SceneModel() {
        availableColors = ColorFactory.zad2Colors();
        selectedColor = availableColors.get(0);
        currentColorIndex = 0;
        triangles = new ArrayList<>();
        points = new ArrayList<>();
    }

    public Color getNextColor() {
        currentColorIndex++;
        if (currentColorIndex > availableColors.size() - 1) {
            currentColorIndex = 0;
        }
        setSelectedColor(availableColors.get(currentColorIndex));
        return selectedColor;
    }

    public Color getPreviousColor() {
        currentColorIndex--;
        if (currentColorIndex < 0) {
            currentColorIndex = availableColors.size() - 1;
        }
        setSelectedColor(availableColors.get(currentColorIndex));
        return selectedColor;
    }

    public void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    private void setSelectedColor(Color color) {
        this.selectedColor = color;
    }

    public void addPoint(Point point) {
        this.points.add(point);
        if (points.size() == 3) {
            addTriangle(new Triangle(selectedColor, points.get(0), points.get(1), points.get(2)));
            points.clear();
        }
    }
}
