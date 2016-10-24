/* This file contains all the input handling.
   Mappings for the keys can be mapped to menu items
   and UI elements for an extension */

import processing.core.PVector;

public final class InputHandler {
    public static void handleMousePressed(PVector mouse) {
        s_prev_mouse.x = mouse.x;
        s_prev_mouse.y = mouse.y;
    }

    public static void handleMouseReleased(MainApplication app, PVector mouse) {
        PVector translate = new PVector(mouse.x - s_prev_mouse.x, mouse.y - s_prev_mouse.y);
        app.ingrainOffsetTranslate(translate);
    }

    public static void handleMouseDragged(MainApplication app, PVector mouse) {
        PVector translate = new PVector(mouse.x - s_prev_mouse.x, mouse.y - s_prev_mouse.y);
        app.offsetTranslates(translate);
    }

    public static void handleKeyPressed(MainApplication app, char key) {
        switch (key) {
            case '+':
                app.setZoom(true);
                break;
            case '-':
                app.setZoom(false);
                break;
            case 'o':
                app.toggleShowOutline();
                break;
            case 'd':
                app.toggleShowDecorate();
                break;
            case 's':
                app.toggleShowStar();
                break;
            case 'h':
                app.adjustStarTopAngle(true);
                break;
            case 'j':
                app.adjustStarTopAngle(false);
                break;
            case 'n':
                app.adjustStarBottomAngle(true);
                break;
            case 'm':
                app.adjustStarBottomAngle(false);
                break;
            case 'f':
                app.adjustStarStrokeWidth(true);
                break;
            case 'g':
                app.adjustStarStrokeWidth(false);
                break;
            case 'v':
                app.adjustNumTiles(true);
                break;
            case 'c':
                app.adjustNumTiles(false);
                break;
            case '1': case '2': case '3': case '4': case '5': case '6':
                app.setStarPattern(Character.getNumericValue(key)-1);
                break;
            default:
                break;
        }
    }

    private static PVector s_prev_mouse = new PVector(0, 0);
}
