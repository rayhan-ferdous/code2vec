        Record[] records = in.getSectionArray(section);

        for (int i = records.length - 1; i >= 0; i--) {

            Record r = records[i];

            removed += r.getWireLength();

            length -= r.getWireLength();

            in.removeRecord(r, section);
