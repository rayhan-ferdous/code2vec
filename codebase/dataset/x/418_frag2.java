        } catch (IOException ioe) {

            return null;

        }

    }



    private static void createAndShowGUI() {

        JFrame frame = new JFrame("ScrollDemo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PDFViewer viewer = new PDFViewer();

        PDFPage page = null;

        File f = null;

        try {

            f = new File("c:/test.pdf");

            page = viewer.openFile(f);
