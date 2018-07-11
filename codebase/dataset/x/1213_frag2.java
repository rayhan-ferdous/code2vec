    private static final byte[] ALPHABET = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };



    /** 

     * Translates a Base64 value to either its 6-bit reconstruction value

     * or a negative number indicating some other meaning.

     **/

    private static final byte[] DECODABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };



    private static final byte BAD_ENCODING = -9;



    private static final byte WHITE_SPACE_ENC = -5;



    private static final byte EQUALS_SIGN_ENC = -1;



    /** Defeats instantiation. */

    private Base64() {

    }



    /**

     * Testing. Feel free--in fact I encourage you--to throw out 

     * this entire "main" method when you actually deploy this code.

     */

    public static void main(String[] args) {

        try {

            {

                byte[] bytes1 = { (byte) 2, (byte) 2, (byte) 3, (byte) 0, (byte) 9 };

                byte[] bytes2 = { (byte) 99, (byte) 2, (byte) 2, (byte) 3, (byte) 0, (byte) 9 };

                System.out.println("Bytes 2,2,3,0,9 as Base64: " + encodeBytes(bytes1));

                System.out.println("Bytes 2,2,3,0,9 w/ offset: " + encodeBytes(bytes2, 1, bytes2.length - 1));

                byte[] dbytes = decode(encodeBytes(bytes1));

                System.out.print(encodeBytes(bytes1) + " decoded: ");

                for (int i = 0; i < dbytes.length; i++) System.out.print(dbytes[i] + (i < dbytes.length - 1 ? "," : "\n"));

            }

            {

                java.io.FileInputStream fis = new java.io.FileInputStream("test.gif.b64");

                Base64.InputStream b64is = new Base64.InputStream(fis, DECODE);

                byte[] bytes = new byte[0];

                int b = -1;

                while ((b = b64is.read()) >= 0) {

                    byte[] temp = new byte[bytes.length + 1];

                    System.arraycopy(bytes, 0, temp, 0, bytes.length);

                    temp[bytes.length] = (byte) b;

                    bytes = temp;

                }

                b64is.close();

                javax.swing.ImageIcon iicon = new javax.swing.ImageIcon(bytes);

                javax.swing.JLabel jlabel = new javax.swing.JLabel("Read from test.gif.b64", iicon, 0);

                javax.swing.JFrame jframe = new javax.swing.JFrame();

                jframe.getContentPane().add(jlabel);

                jframe.pack();

                jframe.show();

                java.io.FileOutputStream fos = new java.io.FileOutputStream("test.gif_out");

                fos.write(bytes);

                fos.close();

                fis = new java.io.FileInputStream("test.gif_out");

                b64is = new Base64.InputStream(fis, ENCODE);

                byte[] ebytes = new byte[0];

                b = -1;

                while ((b = b64is.read()) >= 0) {

                    byte[] temp = new byte[ebytes.length + 1];

                    System.arraycopy(ebytes, 0, temp, 0, ebytes.length);

                    temp[ebytes.length] = (byte) b;

                    ebytes = temp;

                }

                b64is.close();

                String s = new String(ebytes);

                javax.swing.JTextArea jta = new javax.swing.JTextArea(s);

                javax.swing.JScrollPane jsp = new javax.swing.JScrollPane(jta);

                jframe = new javax.swing.JFrame();

                jframe.setTitle("Read from test.gif_out");

                jframe.getContentPane().add(jsp);

                jframe.pack();

                jframe.show();

                fos = new java.io.FileOutputStream("test.gif.b64_out");

                fos.write(ebytes);

                fis = new java.io.FileInputStream("test.gif.b64_out");

                b64is = new Base64.InputStream(fis, DECODE);

                byte[] edbytes = new byte[0];

                b = -1;

                while ((b = b64is.read()) >= 0) {

                    byte[] temp = new byte[edbytes.length + 1];

                    System.arraycopy(edbytes, 0, temp, 0, edbytes.length);

                    temp[edbytes.length] = (byte) b;

                    edbytes = temp;

                }

                b64is.close();

                iicon = new javax.swing.ImageIcon(edbytes);

                jlabel = new javax.swing.JLabel("Read from test.gif.b64_out", iicon, 0);

                jframe = new javax.swing.JFrame();

                jframe.getContentPane().add(jlabel);

                jframe.pack();

                jframe.show();

            }

            {

                java.io.FileInputStream fis = new java.io.FileInputStream("test.gif_out");

                byte[] rbytes = new byte[0];

                int b = -1;

                while ((b = fis.read()) >= 0) {

                    byte[] temp = new byte[rbytes.length + 1];

                    System.arraycopy(rbytes, 0, temp, 0, rbytes.length);

                    temp[rbytes.length] = (byte) b;

                    rbytes = temp;

                }

                fis.close();

                java.io.FileOutputStream fos = new java.io.FileOutputStream("test.gif.b64_out2");

                Base64.OutputStream b64os = new Base64.OutputStream(fos, ENCODE);

                b64os.write(rbytes);

                b64os.close();

                fis = new java.io.FileInputStream("test.gif.b64_out2");

                byte[] rebytes = new byte[0];

                b = -1;

                while ((b = fis.read()) >= 0) {

                    byte[] temp = new byte[rebytes.length + 1];

                    System.arraycopy(rebytes, 0, temp, 0, rebytes.length);

                    temp[rebytes.length] = (byte) b;

                    rebytes = temp;

                }
