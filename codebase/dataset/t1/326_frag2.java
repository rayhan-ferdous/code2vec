    private void doOverwrite(Dataset ds) {

        for (Iterator it = overwrite.iterator(); it.hasNext(); ) {

            DcmElement el = (DcmElement) it.next();

            ds.putXX(el.tag(), el.vr(), el.getByteBuffer());

        }

    }
