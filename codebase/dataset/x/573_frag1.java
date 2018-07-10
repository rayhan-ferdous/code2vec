        RecordNode rn = new RecordNode();

        rn.program = program;

        rn.record = factory.getString();

        rn.record.set(record);

        synchronized (records) {

            while (((RecordNode) records.probe(rn)).program != program) {

                if (locked) {

                    throw new LockedException(0);

                } else {

                    try {

                        records.wait();

                    } catch (InterruptedException ie) {
