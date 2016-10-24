import java.awt.Color;

import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PVector;

class PlaneTiling {
    public PlaneTiling(String name) {
        m_name = name;
        m_num_patterns_width = 10;
        m_num_patterns_height = 10;

        m_upper_star_angle = (float)Math.toRadians(70.5);
        m_lower_star_angle = (float)Math.toRadians(70.5);

        m_upper_star_color = new Color(255, 0, 0);
        m_lower_star_color = new Color(0, 255, 0);

        m_show_star = true;
        m_show_outline = true;
        m_decorate_star = true;

        m_outer_decorate_width = 1.0f;
        m_inner_decorate_width = 0.5f;
        m_outline_width = 1;

        m_foreground_decorate_color = new Color(255, 0, 0);
        m_background_decorate_color = new Color(0, 0, 255);
        m_outline_color = new Color(0, 0, 0);

        m_scale = 120;
    }

    public void setTranslates(PVector p0, PVector p1) {
        this.m_translate0 = p0;
        this.m_translate1 = p1;
    }

    public void setTiling(Tiling tiling) {
        this.m_tiling = tiling;
    }

    public String getName() {
        return this.m_name;
    }

    public void draw(PApplet canvas) {

        // Drawing outlines of what the stars are created from
        if (m_show_outline) {
            drawPlaneOutlines(canvas);
        }

        // Drawing the stars themselves
        if (m_show_star) {
            canvas.noStroke();
            fillPlaneStars(canvas);

            if (m_decorate_star) {
                drawDecoratedStars(canvas);
            }
        }
    }

    // Drawing the outlines that the stars are created from
    private void drawPlaneOutlines(PApplet canvas) {
        for (int w = 0; w < m_num_patterns_width; w++) {
            for (int h = 0; h < m_num_patterns_height; h++) {
                float tx = w * m_translate0.x * m_scale + h * m_translate1.x * m_scale;
                float ty = w * m_translate0.y * m_scale + h * m_translate1.y * m_scale;
                canvas.pushMatrix();
                    canvas.translate(tx, ty);
                    drawOutline(canvas);
                canvas.popMatrix();
            }
        }
    }

    // Drawing the stars outline
    private void drawPlaneStars(PApplet canvas) {
       for (int w = 0; w < m_num_patterns_width; w++) {
            for (int h = 0; h < m_num_patterns_height; h++) {
                float tx = w * m_translate0.x * m_scale + h * m_translate1.x * m_scale;
                float ty = w * m_translate0.y * m_scale + h * m_translate1.y * m_scale;

                float heightratio = (float)h / (float)(m_num_patterns_height-1);
                float starangle = interpolateFloat(m_lower_star_angle, m_upper_star_angle, heightratio);
                m_tiling.setStarAngle(starangle);

                canvas.pushMatrix();
                    canvas.translate(tx, ty);
                    m_tiling.drawStars(canvas);
                canvas.popMatrix();
            }
        }
    }

    // Drawing a filled version of the stars
    private void fillPlaneStars(PApplet canvas) {
        for (int w = 0; w < m_num_patterns_width; w++) {
            for (int h = 0; h < m_num_patterns_height; h++) {
                float tx = w * m_translate0.x * m_scale + h * m_translate1.x * m_scale;
                float ty = w * m_translate0.y * m_scale + h * m_translate1.y * m_scale;

                float heightratio = (float)h / (float)(m_num_patterns_height-1);
                Color starcolor = interpolateColor(m_lower_star_color, m_upper_star_color, heightratio);
                float starangle = interpolateFloat(m_lower_star_angle, m_upper_star_angle, heightratio);
                m_tiling.setStarAngle(starangle);

                canvas.fill(starcolor.getRGB());
                canvas.pushMatrix();
                    canvas.translate(tx, ty);
                    m_tiling.fillStars(canvas);
                canvas.popMatrix();
            }
        }
    }

    private Color interpolateColor(Color c0, Color c1, float ratio) {
        float one_minus_ratio = 1.0f - ratio;

        int r = (int)interpolateFloat(c0.getRed(), c1.getRed(), ratio);
        int g = (int)interpolateFloat(c0.getGreen(), c1.getGreen(), ratio);
        int b = (int)interpolateFloat(c0.getBlue(), c1.getBlue(), ratio);

        return new Color(r, g, b);
    }

    private float interpolateFloat(float f0, float f1, float ratio) {
        float one_minus_ratio = 1.0f - ratio;
        return f0 * ratio + f1 * one_minus_ratio;
    }

    private void drawOutline(PApplet canvas) {
        canvas.strokeWeight(m_outline_width * m_scale / 10);
        canvas.strokeJoin(canvas.BEVEL);
        canvas.stroke(m_outline_color.getRGB());
        m_tiling.drawOutline(canvas);
    }

    private void drawDecoratedStars(PApplet canvas) {
        canvas.noFill();
        canvas.strokeWeight(m_outer_decorate_width * m_scale / 10);
        canvas.strokeJoin(canvas.BEVEL);
        canvas.stroke(m_background_decorate_color.getRGB());
        drawPlaneStars(canvas);

        canvas.strokeWeight(m_inner_decorate_width * m_scale / 10);
        canvas.strokeJoin(canvas.BEVEL);
        canvas.stroke(m_foreground_decorate_color.getRGB());
        drawPlaneStars(canvas);
    }


    /* Getters and Setters */
    public void setNumTiles(int numtiles) {
        if (numtiles < 0) return;
        m_num_patterns_width = numtiles;
        m_num_patterns_height = numtiles;
    }
    public void setShowStar(Boolean show_star) {
        m_show_star = show_star;
    }

    public void setShowOutline(Boolean show_outline) {
        m_show_outline = show_outline;
    }

    public void setDecorateStar(Boolean decorate_star) {
        m_decorate_star = decorate_star;
    }

    public void setUpperStarAngle(float upperangle) {
        if (upperangle <= 0.0 || upperangle >= RADIANS_90) return;
        m_upper_star_angle = upperangle;
    }

    public void setLowerStarAngle(float lowerangle) {
        if (lowerangle <= 0.0 || lowerangle >= RADIANS_90) return;
        m_lower_star_angle = lowerangle;
    }

    public void setDecorateStrokeWidth(float strokewidth) {
        if (strokewidth <= 0.0) return;
        m_outer_decorate_width = strokewidth;
        m_inner_decorate_width = m_outer_decorate_width/2.0f;
    }

    public Boolean getShowOutline() {
        return m_show_outline;
    }

    public Boolean getDecorateStar() {
        return m_decorate_star;
    }

    public Boolean getShowStar() {
        return m_show_star;
    }

    public float getUpperStarAngle() {
        return m_upper_star_angle;
    }

    public float getLowerStarAngle() {
        return m_lower_star_angle;
    }

    public float getDecorateStrokeWidth() {
        return m_outer_decorate_width;
    }

    public int getNumPatterns() {
        return m_num_patterns_width;
    }

    public void debugPrint() {
        System.out.println("PlaneTiling");
        System.out.println("  translate0: " + m_translate0.x + "," + m_translate0.y);
        System.out.println("  translate1: " + m_translate1.x + "," + m_translate1.y);
        System.out.println("  name: " + m_name);
        m_tiling.debugPrint();
    }

    private PVector m_translate0;
    private PVector m_translate1;

    private Tiling m_tiling;
    private String m_name;

    private int m_num_patterns_width;
    private int m_num_patterns_height;
    private float m_upper_star_angle;
    private float m_lower_star_angle;

    private Color m_upper_star_color;
    private Color m_lower_star_color;

    private Boolean m_show_star;
    private Boolean m_show_outline;
    private Boolean m_decorate_star;

    private float m_outer_decorate_width;
    private float m_inner_decorate_width;
    private int m_outline_width;

    private Color m_foreground_decorate_color;
    private Color m_background_decorate_color;
    private Color m_outline_color;

    private float RADIANS_90 = (float)Math.toRadians(90.0);

    private int m_scale;
};
