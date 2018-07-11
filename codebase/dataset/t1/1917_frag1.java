    private void open(File f) {

        try {

            ImageInputStream iis = ImageIO.createImageInputStream(f);

            Iterator iter = ImageIO.getImageReadersByFormatName("DICOM");

            ImageReader reader = (ImageReader) iter.next();

            reader.setInput(iis, false);

            JPanel p = new ImageBox(reader);

            JFrame jf = new JFrame("ImageDisplay - Display Panel");

            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            jf.getContentPane().add(p);

            jf.pack();

            jf.setSize(Math.min(jf.getWidth(), 800), Math.min(jf.getHeight(), 600));

            jf.setVisible(true);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
