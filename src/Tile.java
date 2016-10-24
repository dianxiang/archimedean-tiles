import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PVector;

public abstract class Tile {

    public void drawOutline(PApplet canvas) {
        drawLines(canvas, m_drawlines);
    }

    // Draws in stars with filled colors
    public void fillStars(PApplet canvas) {
        canvas.beginShape();
        drawShape(canvas, m_drawstarlines);
        canvas.endShape();
    }

    public void drawStars(PApplet canvas) {
        drawLines(canvas, m_drawstarlines);
    }

    public void setStarAngle(float angle) {
        generateStarsFromTile(angle);
        applyScaleStar(m_scale);
    }

    public abstract void debugPrint();

    public void applyScale(float scale) {
        PMatrix2D scalemat = new PMatrix2D();
        scalemat.scale(scale);
        applyTransform(m_drawlines, m_drawlines, scalemat);
        applyTransform(m_drawstarlines, m_drawstarlines, scalemat);
    }

    public void applyScaleStar(float scale) {
        PMatrix2D scalemat = new PMatrix2D();
        scalemat.scale(scale);
        applyTransform(m_drawstarlines, m_drawstarlines, scalemat);
    }

    /* Protected functions */
    protected void init(List<PVector> original_lines) {
        m_drawlines = copyLines(original_lines);
        generateStarsFromTile((float)Math.toRadians(70.5));
        applyScale(m_scale);
    }

    protected List<PVector> createPolygon(int s) {
        List<PVector> dst = new ArrayList();
        float oneAngleSide = getAngleSide(s);
        PVector center = new PVector( 1, 1 );

        float angleIncre = TWO_PI / (float)s;

        // Math figures out the radius such that the center of one of the edges
        // of the polygon lies on (0, 1). This is required for the XML file to work
        float radius = (float)(center.y / Math.cos(angleIncre/2));
        float currentAngle = (float)((3*Math.PI/2) - (Math.PI/s));

        for( int i = 0; i < s; i++ ) {
            PVector newPoint = new PVector((float)( center.x + radius * Math.cos(currentAngle) ),
                                         (float)( center.y + radius * Math.sin(currentAngle) ) );
            currentAngle += angleIncre;
            dst.add(newPoint);
        }

        // Moving it back to (0, 0)
        PMatrix2D tOriginal = new PMatrix2D();
        tOriginal.translate(-center.x, -center.y);
        applyTransform(dst, dst, tOriginal);

        return dst;
    }

    protected void applyTransform(List<PVector> src,
                                List<PVector> dst,
                                PMatrix2D transform) {
        PVector tmp = new PVector();
        for( int i = 0; i < src.size(); i++ ) {
            PVector dstpt = dst.get(i);
            PVector srcpt = src.get(i);
            transform.mult(srcpt, tmp);
            dstpt.x = tmp.x;
            dstpt.y = tmp.y;
        }
    }

    protected List<PVector> copyLines(List<PVector> lines) {
        List<PVector> copylist = new ArrayList();
        for (PVector v : lines) {
            copylist.add(v.copy());
        }
        return copylist;
    }

    /* Private functions */
    private void generateStarsFromTile(float angle) {
        if (m_starlines == null) {
            m_starlines = new ArrayList();
        }
        m_starlines.clear();

        for( int i = 0; i < m_lines.size(); i++ ) {
            PVector p0 = m_lines.get(i);
            PVector[] next_two_points = getNextTwoPoints(i, m_lines);
            PVector p1 = next_two_points[0];
            PVector p2 = next_two_points[1];

            // Find the mid point of the polygon edges
            PVector q0 = new PVector((p0.x + p1.x)/ 2.0f,
                                     (p0.y + p1.y) / 2.0f);
            PVector q1 = new PVector((p1.x + p2.x) / 2.0f,
                                     (p1.y + p2.y) / 2.0f);
            // Form a vector based on the given angle
            PVector q0_vec = findAngledVector(p0, p1, angle);
            PVector q1_vec = findAngledVector(p2, p1, -angle+0.01f);

            // Find intersection between these vectors to get the star line
            PVector intersection = new PVector(q0.x, q0.y);
            boolean intersected = intersect(q0, q0_vec,
                                            q1, q1_vec, intersection);

            m_starlines.add(q0);
            m_starlines.add(intersection);
            m_starlines.add(intersection);
            m_starlines.add(q1);
        }

        m_drawstarlines = copyLines(m_starlines);
    }

    private void drawLines(PApplet canvas, List<PVector> lines) {
        for (int l = 0; l < lines.size(); l++) {
            PVector p0 = lines.get(l);
            PVector p1 = getNextLinePoint(lines, l);
            canvas.line(p0.x, p0.y, p1.x, p1.y);
        }
    }

    private void drawShape(PApplet canvas, List<PVector> vertices) {
      for (PVector p : vertices) {
            canvas.vertex(p.x, p.y);
        }
    }

    private PVector getNextLinePoint(List<PVector> lines, int l) {
        if (l == lines.size() - 1) {
          return lines.get(0);
        } else {
          return lines.get(l+1);
        }
    }

    // Equation to specify angles between each vertex of a regular polygon
    // http://www.mathopenref.com/polygoninteriorangles.html
    private float getAngleSide(int sides) {
          return (float)(((float)sides-2)*Math.PI) / ((float)sides);
    }

    // Find whether two vectors (given points) exists and returns it in ret
    private boolean intersect(PVector p1, PVector v1,
                              PVector p2, PVector v2,
                              PVector ret) {
        float denom = ( ( v1.x * v2.y ) - ( v1.y * v2.x ) );
        if( denom == 0 ) return false;

        float s = (( v1.x * (p1.y - p2.y) - (v1.y * p1.x ) + (v1.y * p2.x))) / denom;
        ret.x = p2.x + s * v2.x;
        ret.y = p2.y + s * v2.y;
        return true;
    }

    // Given a line and the angle, finds the vector that starts at p1 and rotates angle away
    private PVector findAngledVector(PVector p1,
                                     PVector p2,
                                     float angle) {
        PVector vec = new PVector( p2.x - p1.x,
                                   p2.y - p1.y );
        PVector ret = new PVector();
        PMatrix2D rotateMat = new PMatrix2D();
        rotateMat.rotate(angle);
        rotateMat.mult(vec, ret);

        return ret;
    }

    private PVector[] getNextTwoPoints(int i, List<PVector> points) {
        PVector [] ret;
        if( i == points.size() - 1 ) {
            ret = new PVector[] {points.get(0), points.get(1)};
        } else if( i == points.size() - 2 ) {
            ret =  new PVector[] {points.get(i+1), points.get(0)};
        } else {
            ret = new PVector[] {points.get(i+1), points.get(i+2)};
        }

        return ret;
    }


    /* Protected member variables */
    protected PMatrix2D m_transform;
    // Lines for the base shape
    protected List<PVector> m_lines;

    /* Private member variables */
    private List<PVector> m_starlines;

    private List<PVector> m_drawlines;
    private List<PVector> m_drawstarlines;

    private static final float TWO_PI = (float)Math.PI * 2.0f;
    private int m_scale = 120;

};
