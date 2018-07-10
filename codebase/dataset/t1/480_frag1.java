        while (crossings != RECT_INTERSECTS && !pi.isDone()) {

            switch(pi.currentSegment(coords)) {

                case PathIterator.SEG_MOVETO:

                    if (curx != movx || cury != movy) {

                        crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);

                    }

                    movx = curx = coords[0];

                    movy = cury = coords[1];

                    break;

                case PathIterator.SEG_LINETO:

                    endx = coords[0];

                    endy = coords[1];

                    crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, endx, endy);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_QUADTO:

                    endx = coords[2];

                    endy = coords[3];

                    crossings = rectCrossingsForQuad(crossings, rxmin, rymin, rxmax, rymax, curx, cury, coords[0], coords[1], endx, endy, 0);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_CUBICTO:

                    endx = coords[4];

                    endy = coords[5];

                    crossings = rectCrossingsForCubic(crossings, rxmin, rymin, rxmax, rymax, curx, cury, coords[0], coords[1], coords[2], coords[3], endx, endy, 0);

                    curx = endx;

                    cury = endy;

                    break;

                case PathIterator.SEG_CLOSE:

                    if (curx != movx || cury != movy) {

                        crossings = rectCrossingsForLine(crossings, rxmin, rymin, rxmax, rymax, curx, cury, movx, movy);

                    }

                    curx = movx;

                    cury = movy;

                    break;

            }

            pi.next();

        }

        if (crossings != RECT_INTERSECTS && (curx != movx || cury != movy)) {
