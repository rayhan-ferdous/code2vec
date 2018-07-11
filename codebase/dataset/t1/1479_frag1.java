    private byte[] getFileData(String fileName) throws Exception {

        byte[] data = null;

        for (Attachment att : filePack.keySet()) {

            if (att.getName().equals(fileName)) {

                data = att.getData();

                break;

            }

        }

        return data;

    }
