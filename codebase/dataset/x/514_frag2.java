        if (!ds.contains(Tags.SOPInstanceUID)) {

            ds.putUI(Tags.SOPInstanceUID, uidgen.createUID());

        }

    }



    private static void addContentDateTime(Dataset ds) {

        if (!ds.contains(Tags.ContentDate)) {

            Date now = new Date();

            ds.putDA(Tags.ContentDate, now);
