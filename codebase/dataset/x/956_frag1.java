    public MultipartElement getNextElement() throws IOException {

        MultipartElement element = null;

        if (!isMaxLengthExceeded()) {

            if (!this.inputStream.isFinalBoundaryEncountered()) {

                if (this.inputStream.isElementFile()) {

                    element = createFileMultipartElement();

                } else {

                    String encoding = getElementEncoding();

                    element = createTextMultipartElement(encoding);

                }

                this.inputStream.resetForNextBoundary();

            }

        }

        return element;

    }
