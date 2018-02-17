package hr.fer.zemris.irg.algortihms;

import hr.fer.zemris.irg.models.Line;

import java.awt.Point;
import java.util.List;

/**
 * @author Filip Gulan
 */
public interface ILineClipper {
    List<Point> calculateInterceptionPoints(Line line);
}
