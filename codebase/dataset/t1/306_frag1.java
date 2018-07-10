    public short getNumPoints(short glyphIndex, short contourIndex) {

        if (!loadingCompleted) {

            throw new IllegalStateException("Loading NOT completed!");

        }

        if (numGlyphs > 0 && 0 <= glyphIndex && glyphIndex < numGlyphs && glyphs[glyphIndex].numContours > 0 && contourIndex < glyphs[glyphIndex].numContours) {

            return glyphs[glyphIndex].contour[contourIndex].numPoints;

        } else {

            return 0;

        }

    }
