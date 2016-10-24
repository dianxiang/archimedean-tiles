import java.awt.Color;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.XML;

class MainApplication {

    public MainApplication(PApplet canvas) {
        XML arch_xml = TilingXML.getXmlFromFile(canvas, this.ARCH_FILENAME);
        m_arch_list = TilingXML.parseArchXml(arch_xml);
        m_canvas = canvas;
        setStarPattern(0);

        /* Ui variables */
        m_offset_translate = new PVector(0, 0);
        m_prev_offset_translate = new PVector(0, 0);
        m_zoom = 1;
    }

    public void draw() {
        // Offsets for mouse dragging through the canvas
        m_canvas.translate(m_prev_offset_translate.x + m_offset_translate.x,
                           m_prev_offset_translate.y + m_offset_translate.y);
        m_canvas.scale(m_zoom);

        // Draw the background
        m_canvas.background(getBackgroundColor().getRGB());

        // Draw the tiles and stars
        if (m_current_tiling != null) {
            m_current_tiling.draw(m_canvas);
        }
    }

    public Color getBackgroundColor() {
        return BACKGROUND_COLOR;
    }

    /* UI functions */
    public void offsetTranslates(PVector translate) {
        m_offset_translate.set(translate.x, translate.y);
    }

    public void ingrainOffsetTranslate(PVector translate) {
        m_prev_offset_translate.x += m_offset_translate.x;
        m_prev_offset_translate.y += m_offset_translate.y;

        m_offset_translate.set(0, 0);
    }

    public void setZoom(Boolean increase) {
        m_zoom = increase ? m_zoom * ZOOM_RATIO : m_zoom / ZOOM_RATIO;
    }

    public void toggleShowOutline() {
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setShowOutline(!planetiling.getShowOutline());
        }
    }

    public void toggleShowDecorate() {
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setDecorateStar(!planetiling.getDecorateStar());
        }
    }

    public void toggleShowStar() {
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setShowStar(!planetiling.getShowStar());
        }
    }

    public void adjustStarTopAngle(Boolean increase) {
        float angle = increase ? ANGLE_ADJ : -ANGLE_ADJ;
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setUpperStarAngle(planetiling.getUpperStarAngle() + angle);
        }
    }

    public void adjustStarBottomAngle(Boolean increase) {
        float angle = increase ? ANGLE_ADJ : -ANGLE_ADJ;
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setLowerStarAngle(planetiling.getLowerStarAngle() + angle);
        }
    }

    public void adjustStarStrokeWidth(Boolean increase) {
        float width = increase ? STROKE_ADJ : -STROKE_ADJ;
        for (PlaneTiling planetiling : m_arch_list) {
            planetiling.setDecorateStrokeWidth(planetiling.getDecorateStrokeWidth() + width);
        }
    }

    public void adjustNumTiles(Boolean increase) {
        for (PlaneTiling planetiling : m_arch_list) {
            int num_tiles_adj = increase ? 10 : -10;
            planetiling.setNumTiles(planetiling.getNumPatterns() + num_tiles_adj);
        }
    }

    public void setStarPattern(int tiling_index) {
        m_current_tiling_index = tiling_index;
        m_current_tiling = m_arch_list.get(m_current_tiling_index);
    }

    // List of archimedean tilings and their plan tiling representations
    private List<PlaneTiling> m_arch_list;
    // The canvas to draw on. Processing API
    private PApplet m_canvas;
    // The currently shown archimedean tile index
    private int m_current_tiling_index;
    // The currently shown archimedean tile
    private PlaneTiling m_current_tiling;

    // UI variables for zooming and dragging
    private PVector m_offset_translate;
    private PVector m_prev_offset_translate;
    private float m_zoom;

    // Ratios or increments to adjust by
    private float ANGLE_ADJ = (float)Math.toRadians(1.0);
    private float STROKE_ADJ = 0.1f;
    private static float ZOOM_RATIO = 1.03f;

    private static Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static String ARCH_FILENAME = "data/Archimedeans.xml";
}
