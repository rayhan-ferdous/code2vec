                continue;

            }

            ChordShape.Fretting f = shape.getFretting(i - firstString);

            if (f.finger == -1) {

                string.hold(f.fret);

            } else if (offset + f.fret >= 0) {

                string.hold(offset + f.fret);
