import java.util.List;

import processing.core.PMatrix2D;
import processing.core.PVector;

public class TileFactory {
  public static Tile createRegularTile(int sides, PMatrix2D transform) {
    return new RegularTile(sides, transform);
  }

  public static Tile createPolygonTile(List<PVector> polygonPoints, PMatrix2D transform) {
    return new PolygonTile(polygonPoints, transform);
  }
}
