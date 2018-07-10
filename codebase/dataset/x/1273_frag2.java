            in.removeRecord(r, section);

            if (length > maxLength) continue; else {

                for (int j = i - 1; j >= 0; j--) {

                    Record r2 = records[j];

                    if (!r.getName().equals(r2.getName()) || r.getType() != r2.getType() || r.getDClass() != r2.getDClass()) break;

                    removed += r2.getWireLength();

                    length -= r2.getWireLength();

                    in.removeRecord(r2, section);
