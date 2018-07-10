            out.flush();

            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;

            StringBuffer buffer = new StringBuffer();

            while ((line = in.readLine()) != null) {

                buffer.append(line);
