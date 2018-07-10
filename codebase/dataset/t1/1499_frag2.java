    public void truncate(Message in, int length, int maxLength) {

        TSIGRecord tsig = in.getTSIG();

        if (tsig != null) maxLength -= tsig.getWireLength();

        length -= truncateSection(in, maxLength, length, Section.ADDITIONAL);

        if (length < maxLength) return;

        in.getHeader().setFlag(Flags.TC);

        if (tsig != null) {

            in.removeAllRecords(Section.ANSWER);

            in.removeAllRecords(Section.AUTHORITY);

            return;

        }

        length -= truncateSection(in, maxLength, length, Section.AUTHORITY);

        if (length < maxLength) return;

        length -= truncateSection(in, maxLength, length, Section.ANSWER);

    }
