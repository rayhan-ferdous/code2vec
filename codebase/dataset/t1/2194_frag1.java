    private int getOffsetOfNextLine(org.eclipse.jface.text.IDocument document, int offset) {

        int end = document.getLength();

        int nextLineOffset = offset;

        if (offset < 0 || offset > end) {

            return -1;

        }

        while (nextLineOffset < end) {

            String charAtOffset = "";

            try {

                charAtOffset += document.getChar(nextLineOffset);

            } catch (org.eclipse.jface.text.BadLocationException e) {

                return -1;

            }

            if (charAtOffset.matches("\\S")) {

                return nextLineOffset;

            }

            if (charAtOffset.equals("\n")) {

                return nextLineOffset + 1;

            }

            nextLineOffset++;

        }

        return offset;

    }

}
