        if (remoteFile.length() > 0) {

            byte[] unzippedXml = FileAccess.readZipFileBinary(remoteFile);

            FileAccess.writeByteArray(unzippedXml, remoteFile);

            return checkLocalMessage(remoteFile);

        } else {

            return false;

        }

    }



    /**

	 * This method composes the downloading key for the message, given a

	 * certain index number

	 * @param index index number to use to compose the key

	 * @return they composed key

	 */

    private String composeDownKey(int index) {

        String key;

        if (secure) {

            key = new StringBuffer().append(publicKey).append("/").append(board.getBoardFilename()).append("/").append(message.getDate()).append("-").append(index).append(".xml").toString();
