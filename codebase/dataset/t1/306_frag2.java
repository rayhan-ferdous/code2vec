    public short getGlyphXMin(short glyphIndex) {

        if (!loadingCompleted) {

            throw new IllegalStateException("Loading NOT completed!");

        }

        if (numGlyphs > 0 && 0 <= glyphIndex && glyphIndex < numGlyphs) {

            return glyphs[glyphIndex].xMin;

        } else {

            return 0;

        }

    }
