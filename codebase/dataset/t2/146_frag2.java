    public boolean contains(double x, double y, double w, double h) {

        if (!getBounds2D().intersects(x, y, w, h)) return false;

        if (getAxisIntersections(x, y, true, w) != 0 || getAxisIntersections(x, y + h, true, w) != 0 || getAxisIntersections(x + w, y, false, h) != 0 || getAxisIntersections(x, y, false, h) != 0) return false;

        if ((getAxisIntersections(x, y, true, BIG_VALUE) & 1) != 0) return true;

        return false;

    }



    /**

   * Determines whether a Rectangle2D is entirely inside the area that is 

   * bounded by the curve and the straight line connecting its end points.

   * @see #contains(double, double, double, double)

   */

    public boolean contains(Rectangle2D r) {
