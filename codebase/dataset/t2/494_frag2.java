    public void test_read_$BII_IOException() throws IOException {

        FileOutputStream fos = new java.io.FileOutputStream(fileName);

        fos.write(fileString.getBytes(), 0, fileString.length());

        fos.close();

        RandomAccessFile raf = new java.io.RandomAccessFile(fileName, "r");

        byte[] rbuf = new byte[100];
