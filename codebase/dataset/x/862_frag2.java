                ressource.println(project);

                ressource.println(app);

                try {

                    String value;

                    StringTokenizer st_equal, st_newline = new StringTokenizer(export, "\n\r\f");

                    while (st_newline.hasMoreTokens()) {

                        st_equal = new StringTokenizer(st_newline.nextToken(), "= \t");

                        ressource.print(st_equal.nextToken() + "=");
