package hr.fer.zemris.irg.factory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Gulan
 */
public class ColorFactory {

    public static List<Color> zad2Colors() {
        List<Color> colors = new ArrayList<>();
        // Red
        colors.add(new Color(255, 0, 0));
        // Green
        colors.add(new Color(0, 255, 0));
        // Blue
        colors.add(new Color(0, 0, 255));
        // Cyan
        colors.add(new Color(0, 255, 255));
        // Yellow
        colors.add(new Color(255, 255, 0));
        // Magenta
        colors.add(new Color(255, 0, 255));
        return colors;
    }
}
