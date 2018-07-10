    public static class InputStream extends java.io.FilterInputStream {



        private boolean encode;



        private int position;



        private byte[] buffer;



        private int bufferLength;



        private int numSigBytes;



        private int lineLength;



        private boolean breakLines;



        private int options;



        @SuppressWarnings("unused")

        private byte[] alphabet;



        private byte[] decodabet;
