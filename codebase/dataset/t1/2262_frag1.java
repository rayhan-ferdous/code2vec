    private static void addUIDs(Dataset ds) {

        UIDGenerator uidgen = UIDGenerator.getInstance();

        if (!ds.contains(Tags.StudyInstanceUID)) {

            ds.putUI(Tags.StudyInstanceUID, uidgen.createUID());

        }

        if (!ds.contains(Tags.SeriesInstanceUID)) {

            ds.putUI(Tags.SeriesInstanceUID, uidgen.createUID());

        }

        if (!ds.contains(Tags.SOPInstanceUID)) {

            ds.putUI(Tags.SOPInstanceUID, uidgen.createUID());

        }

    }
