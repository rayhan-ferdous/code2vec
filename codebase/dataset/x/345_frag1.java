        stamper.setFullCompression();

        for (int i = 0; i < reader.getNumberOfPages() + 1; i++) {

            PdfContentByte overContent = stamper.getOverContent(i);

            if (overContent != null) {

                overContent.beginText();

                font = FontFactory.getFont(FontFactory.TIMES_ITALIC);
