            URL url = new URL(jEdit.getProperty("version-check.url"));

            InputStream in = url.openStream();

            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            String line;

            String version = null;

            String build = null;

            while ((line = bin.readLine()) != null) {

                if (line.startsWith(".version")) version = line.substring(8).trim(); else if (line.startsWith(".build")) build = line.substring(6).trim();

            }

            bin.close();

            if (version != null && build != null) {
