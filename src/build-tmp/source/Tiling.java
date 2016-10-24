import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

class Tiling {

    public Tiling() {
        m_tiles = new ArrayList();
    }

    public void addTile(Tile tile) {
        m_tiles.add(tile);
    }

    public void drawOutline(PApplet canvas) {
        for (Tile tile : m_tiles) {
            tile.drawOutline(canvas);
        }
    }

    public void drawStars(PApplet canvas) {
        for (Tile tile : m_tiles) {
            tile.drawStars(canvas);
        }
    }

    public void fillStars(PApplet canvas) {
        for (Tile tile : m_tiles) {
            tile.fillStars(canvas);
        }
    }

    public void setStarAngle(float starangle) {
        for (Tile tile : m_tiles) {
            tile.setStarAngle(starangle);
        }
    }

    public void debugPrint() {
        System.out.println("Tiling");
        System.out.println("  numtiles: " + m_tiles.size());
        for(Tile tile : m_tiles) {
            tile.debugPrint();
        }
    }

    private List<Tile> m_tiles;
};