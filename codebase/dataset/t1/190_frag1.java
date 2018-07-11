    private Image getResourceImage(String name) {

        String resource_name = images.getString(name);

        try {

            return Resource.createImage(resource_name, this);

        } catch (IOException e) {

            System.err.println(e);

        }

        return null;

    }
