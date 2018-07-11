    public void writeDynData(QuadTree.Rect bounds, ByteBuffer out) {

        this.execute(bounds, this.new WriteDataExecutor(out, false));

        for (OTFDataWriter element : this.additionalElements) {

            try {

                element.writeDynData(out);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }
