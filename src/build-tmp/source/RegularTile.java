import processing.core.PMatrix2D;

public class RegularTile extends Tile {

    public RegularTile(int sides,
                       PMatrix2D transform) {
        m_sides = sides;
        m_transform = transform;
        m_lines = this.createPolygon(m_sides);
        applyTransform(m_lines, m_lines, transform);
        init(m_lines);
    }

    public void debugPrint() {
        System.out.println("RegularTile");
        System.out.println("  sides: " + m_sides);
        System.out.println("  transform: " + m_transform);
    }

    private int m_sides;
};