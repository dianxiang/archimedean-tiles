import java.util.List;

import processing.core.PMatrix2D;
import processing.core.PVector;

public class PolygonTile extends Tile {

    public PolygonTile(List<PVector> polygonPoints,
                       PMatrix2D transform) {
        m_lines = copyLines(polygonPoints);
        m_transform = transform;
        applyTransform(m_lines, m_lines, transform);
        init(m_lines);
    }

    public void debugPrint() {
        System.out.println("PolygonTile");
        System.out.println("  transform: " + m_transform);
    }
}