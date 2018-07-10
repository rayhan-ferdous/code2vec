    public static void writeContentToFile(IDfSysObject document, String path) throws DfException, IOException {

        String fileName = document.getObjectName();

        String fileExtension = null;

        if (fileName.indexOf(".") < 0) {

            FormatHelper formatHelper = FormatHelper.getInstance(document.getSession());

            fileExtension = formatHelper.getExtensionForFormat(document.getContentType());

            fileName = fileName + "." + fileExtension;

        }

        writeContentToFile(document, path, fileName);

    }
