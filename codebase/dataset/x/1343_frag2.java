    public static void appendToFile(String fullname, String appendto, boolean keepopen) {

        RandomAccessFile lw = null;

        try {

            lw = getFile(fullname);

            if (lw == null) {

                File f = new File(fullname);

                lw = new RandomAccessFile(f, "rw");

                if (keepopen) m_files.put(fullname, lw);

            }

            lw.seek(lw.length());

            lw.writeBytes(appendto);

        } catch (Throwable e) {

            print(e);

        } finally {

            if (lw != null && !keepopen) try {

                lw.close();

            } catch (java.io.IOException ioe) {

            }

        }

    }
