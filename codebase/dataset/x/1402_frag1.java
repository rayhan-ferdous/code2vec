    public org.apollo.datamodel.Range getCORBARange(SeqFeatureI in) {

        Range range = new Range();

        range.range_min = (int) in.getLow();

        range.range_max = (int) in.getHigh();

        range.sequence_id = extId;

        if (in.getStrand() < 0) {

            range.strand = StrandType.minus;

            System.out.println("Minus");

        } else if (in.getStrand() > 0) {

            range.strand = StrandType.plus;

            System.out.println("Plus");

        } else {

            range.strand = StrandType.nulltype;

            System.out.println("Null");

        }

        return range;

    }
