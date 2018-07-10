    public boolean getBooleanFieldValue(int rowIndex, int fieldId) {

        int recordOffset = (myHeader.getRecordLength() * rowIndex) + myHeader.getHeaderLength() + 1;

        int fieldOffset = 0;

        for (int i = 0; i < (fieldId - 1); i++) {

            fieldOffset += myHeader.getFieldLength(i);

        }

        buffer.position(recordOffset + fieldOffset);

        char bool = (char) buffer.get();

        return ((bool == 't') || (bool == 'T') || (bool == 'Y') || (bool == 'y'));

    }
