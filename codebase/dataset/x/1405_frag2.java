    public void saveCache() {

        if (jarfiles == null || !indexModified) {

            return;

        }

        indexModified = false;

        comment("writing modified index file");

        try {

            DataOutputStream ostream = outOpenIndex();

            for (Entry<String, JarXEntry> entry : jarfiles.entrySet()) {

                String jarcanon = entry.getKey();

                JarXEntry xentry = entry.getValue();

                ostream.writeUTF(jarcanon);

                ostream.writeUTF(xentry.cachefile);

                ostream.writeLong(xentry.mtime);

            }

            ostream.close();

        } catch (IOException ioe) {

            warning("can't write index file");

        }

    }
