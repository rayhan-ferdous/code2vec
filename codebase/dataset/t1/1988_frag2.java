    public boolean start(OutputStream os) {

        if (os == null) return false;

        boolean ok = true;

        closeStream = false;

        out = os;

        try {

            writeString("GIF89a");

        } catch (IOException e) {

            ok = false;

        }

        return started = ok;

    }
