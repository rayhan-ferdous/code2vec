            if ((host == null) || (host.equals(""))) host = DEFAULT_HOST;

            if (form == null) form = DEFAULT_FORM;

            if (data == null) throw new IllegalArgumentException("Invalid data");

            URL url = new URL(protocol, host, form);

            URLConnection con = url.openConnection();

            con.setDoOutput(true);
