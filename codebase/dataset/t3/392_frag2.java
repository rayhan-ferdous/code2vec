    public double getMaxY(double x1, double x2) {

        if (x1 > x2) throw new IllegalArgumentException("X values are out of order");

        if (x2 >= maxX) throw new IndexOutOfBoundsException("X(" + x2 + ") is not valid for function");

        if (x1 == x2) return getY(x1);

        int idx = indexOfSegment(x1);

        double max = Double.NEGATIVE_INFINITY;

        do {

            double yMax = pointY[idx];

            double slope = pointSlope[idx];

            if (slope != 0) {

                double xs = pointX[idx];

                if (slope > 0) {

                    double xe = pointX[idx + 1];

                    double lastX = Math.min(x2, xe);

                    yMax = calculateY(xs, yMax, slope, lastX);

                } else if (x1 > xs) {

                    yMax = calculateY(xs, yMax, slope, x1);

                }

            }

            if (yMax > max) max = yMax;

            idx++;

        } while (idx < segmentCount && idx <= indexOfSegmentEnd(idx, x2));

        return max;

    }
