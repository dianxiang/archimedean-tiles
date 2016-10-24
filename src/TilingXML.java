import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PVector;
import processing.data.XML;

class TilingXML {
    public static XML getXmlFromFile(PApplet utilities, String filename) {
        return utilities.loadXML(filename);
    }

    public static List<PlaneTiling> parseArchXml(XML archxml) {
        List<PlaneTiling> tilings_list = new ArrayList();

        XML[] tilings_xml = archxml.getChildren("tiling");
        for (XML plane_tiling_xml : tilings_xml) {
            tilings_list.add(parsePlaneTiling(plane_tiling_xml));
        }

        return tilings_list;
    }

    private static PlaneTiling parsePlaneTiling(XML plane_tiling_xml) {
        // Translations that offsets each tiling to fill the plane
        XML[] translations = plane_tiling_xml.getChildren("translations");
        if (translations.length != NUM_ARCH_TRANSLATIONS) return null;

        // Vector components of the translations
        XML[] translation_vectors = translations[0].getChildren("vector");
        if (translation_vectors.length != NUM_ARCH_TRANSLATION_VECTORS) return null;

        PVector t0 = archXmlToPVector(translation_vectors[0]);
        PVector t1 = archXmlToPVector(translation_vectors[1]);

        PlaneTiling new_plane_tiling = new PlaneTiling(
            plane_tiling_xml.getString("name"));

        new_plane_tiling.setTranslates(t0, t1);

        // Planes tilings consists of a repeated number of tilings
        XML[] tiles_xml = plane_tiling_xml.getChildren("tile");
        Tiling tiling = parseTiling(tiles_xml);
        new_plane_tiling.setTiling(tiling);

        return new_plane_tiling;
    }

    private static Tiling parseTiling(XML[] tiles_xml) {
        // Tilings consists of a repeated number of transformed tiles
        Tiling new_tiling = new Tiling();
        for (XML tile_xml : tiles_xml) {
            XML[] shapes = tile_xml.getChildren("shape");
            if (shapes.length != NUM_SHAPES_PER_TILE) return null;
            XML shape = shapes[0];

            String shapetype = shape.getString("type");
            if (shapetype.equals(ARCH_TYPE_REGULAR)) {
                parseAndAddArchTileRegular(tile_xml, shape.getInt("sides"), new_tiling);
            } else if (shapetype.equals(ARCH_TYPE_POLYGON)) {
                parseAndAddArchTilePolygon(tile_xml, shape, new_tiling);
            } else {
                return null;
            }
        }

        return new_tiling;
    }

    private static void parseAndAddArchTileRegular(
            XML xml_tile_regular, int sides, Tiling new_tiling_dst) {
        XML[] transforms = xml_tile_regular.getChildren("transform");

        for (XML transform : transforms) {
            PMatrix2D transform_matrix = archXmlToPMatrix2D(transform);
            Tile new_tile = TileFactory.createRegularTile(sides, transform_matrix);
            new_tiling_dst.addTile(new_tile);
        }
    }

    private static void parseAndAddArchTilePolygon(
            XML xml_tile_polygon, XML shape, Tiling new_tiling_dst) {
        List<PVector> vertices = new ArrayList();
        XML[] xml_vertices = shape.getChildren("vertex");
        for (XML xml_vertex : xml_vertices) {
            PVector new_vertex = new PVector(xml_vertex.getFloat("x"),
                                             xml_vertex.getFloat("y"));
            vertices.add(new_vertex);
        }

        XML[] transforms = xml_tile_polygon.getChildren("transform");
        for (XML transform : transforms) {
            PMatrix2D transform_matrix = archXmlToPMatrix2D(transform);
            Tile new_tile = TileFactory.createPolygonTile(vertices, transform_matrix);
            new_tiling_dst.addTile(new_tile);
        }
    }

    private static PVector archXmlToPVector(XML xml_vector) {
        return new PVector(xml_vector.getFloat("x"), xml_vector.getFloat("y"));
    }

    private static PMatrix2D archXmlToPMatrix2D(XML xml_matrix) {
        return new PMatrix2D(xml_matrix.getFloat("a"),
                             xml_matrix.getFloat("b"),
                             xml_matrix.getFloat("c"),
                             xml_matrix.getFloat("d"),
                             xml_matrix.getFloat("e"),
                             xml_matrix.getFloat("f"));
    }

    private static int NUM_ARCH_TRANSLATIONS = 1;
    private static int NUM_ARCH_TRANSLATION_VECTORS = 2;
    private static int NUM_SHAPES_PER_TILE = 1;
    private static String ARCH_TYPE_REGULAR = "regular";
    private static String ARCH_TYPE_POLYGON = "polygon";

}
