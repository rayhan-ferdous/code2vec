            StringBuilder buf = new StringBuilder();

            String str;

            while ((str = reader.readLine()) != null) {

                buf.append(str);

                buf.append(System.getProperty("line.separator"));

            }

            reader.close();

            res = new ResponseXML(buf.toString());
