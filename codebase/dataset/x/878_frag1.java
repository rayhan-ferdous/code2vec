                    String error = new String(imageBytes);

                    int pos = error.indexOf("<?xml");

                    if (pos != -1) {

                        String xml = error.substring(pos, error.length());

                        exceptionMessage = parseException(xml.getBytes());

                        if (exceptionMessage == null) exceptionMessage = new String(imageBytes);
