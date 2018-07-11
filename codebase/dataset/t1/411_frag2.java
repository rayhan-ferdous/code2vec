    private void applyUIDSuffix(Dataset ds) {

        ds.putUI(Tags.StudyInstanceUID, ds.getString(Tags.StudyInstanceUID, "") + uidSuffix);

        ds.putUI(Tags.SeriesInstanceUID, ds.getString(Tags.SeriesInstanceUID, "") + uidSuffix);

        ds.putUI(Tags.SOPInstanceUID, ds.getString(Tags.SOPInstanceUID, "") + uidSuffix);

    }
