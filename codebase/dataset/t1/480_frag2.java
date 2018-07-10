        while (!pi.isDone()) {

            switch(pi.currentSegment(coords)) {

                case PathIterator.SEG_MOVETO:

                    if (cury != movy) {

                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);

                    }

                    movx = curx = coords[0];

                    movy = cury = coords[1];

                    break;

                case PathIterator.SEG_LINETO:

                    endx = coords[0];

                    endy = coords[1];

                    crossings += pointCrossingsForLine(px, py, curx, cury, endx, endy);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_QUADTO:

                    endx = coords[2];

                    endy = coords[3];

                    crossings += pointCrossingsForQuad(px, py, curx, cury, coords[0], coords[1], endx, endy, 0);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_CUBICTO:

                    endx = coords[4];

                    endy = coords[5];

                    crossings += pointCrossingsForCubic(px, py, curx, cury, coords[0], coords[1], coords[2], coords[3], endx, endy, 0);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_CLOSE:

                    if (cury != movy) {

                        crossings += pointCrossingsForLine(px, py, curx, cury, movx, movy);

                    }

                    curx = movx;

                    cury = movy;

                    break;

            }

            pi.next();

        }

        if (cury != movy) {
