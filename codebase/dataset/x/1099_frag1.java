    private String saveImageToFile(InputStream inputStream, String imageRelativePath) {

        OutputStream outputStream;

        String destination = "";

        try {

            destination = path(DOCROOT_EXT, imageRelativePath);

            String destinationRoot = destination.substring(0, destination.lastIndexOf("/"));

            File df = new File(destinationRoot);

            if (!df.exists()) df.mkdir();

            outputStream = new FileOutputStream(destination);

            fromInputToOutputStream(inputStream, outputStream);

            publishToWeb(destination, COPY_TYPE);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        return destination;

    }
