        char c;

        do {

            c = readChar(dis);

        } while (Character.isWhitespace(c));

        do {

            sb.append(c);

            c = readChar(dis);

        } while (!Character.isWhitespace(c));

        return sb.toString();

    }



    /**

     * Reads a single char from the stream

     * 

     * @param dis

     *                the stream to read

     * @return the next character on the stream

     * 

     * @throws IOException

     *                 if an error occurs

     */

    private char readChar(DataInputStream dis) throws IOException {

        return (char) dis.readByte();
