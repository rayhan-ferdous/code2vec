            if (!SBCMain.DEBUG_MODE) to = new FileOutputStream(toFile); else to = new FileOutputStream(toF);

            byte[] buffer = new byte[4096];

            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1) to.write(buffer, 0, bytesRead);

        } finally {

            if (from != null) try {

                from.close();

            } catch (IOException e) {

                ;

            }

            if (to != null) try {

                to.close();

            } catch (IOException e) {

                ;

            }

        }
