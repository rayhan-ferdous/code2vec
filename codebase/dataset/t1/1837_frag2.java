        URL jarURL = null;

        JarFile jar = null;

        try {

            jarURL = new URL("file:/" + file.getCanonicalPath());

            jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");

            JarURLConnection conn = (JarURLConnection) jarURL.openConnection();

            jar = conn.getJarFile();

        } catch (Exception e) {
