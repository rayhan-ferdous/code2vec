                StringBuilder contents = new StringBuilder();

                String line;

                while ((line = urlReader.readLine()) != null) {

                    contents.append(line);

                    contents.append(System.getProperty("line.separator"));

                }

                urlReader.close();

                return contents.toString();
