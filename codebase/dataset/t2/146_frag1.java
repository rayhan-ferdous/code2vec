    public boolean intersects(double x, double y, double w, double h) {

        if (!getBounds2D().contains(x, y, w, h)) return false;

        if (getAxisIntersections(x, y, true, w) != 0 || getAxisIntersections(x, y + h, true, w) != 0 || getAxisIntersections(x + w, y, false, h) != 0 || getAxisIntersections(x, y, false, h) != 0) return true;

        if ((getAxisIntersections(x, y, true, BIG_VALUE) & 1) != 0) return true;

        return false;

    }



    /**

   * Determines whether any part of a Rectangle2D is inside the area bounded 

   * by the curve and the straight line connecting its end points.

   * @see #intersects(double, double, double, double)

   */

    public boolean intersects(Rectangle2D r) {
