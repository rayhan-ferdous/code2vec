    public boolean writer__endFile() {

        boolean endOK = true;

        String fileName = _getFullFileName();

        String filePath = writer__iOutputFolder + "\\" + fileName;

        if (writer__iBackup) {

            try {

                writer__backup(filePath, fileName, writer__iBackupFolder);

            } catch (Exception e) {

                writer__handleException(writer__UITEXT_UnableToBackupFile + filePath + writer__UITEXT_ToBackupFolder + writer__iBackupFolder, e);

                endOK = false;

            }

        }

        if (endOK && writer__iSave) {

            try {

                writer__save(filePath, writer__iCurrentText.toString());

                writer__userMessage(writer__UITEXT_SavedFile + filePath + writer__UITEXT_NewLine);

            } catch (Exception e) {

                writer__handleException(writer__UITEXT_UnableToSaveFile + filePath, e);

                endOK = false;

            }

        }

        return endOK;

    }
