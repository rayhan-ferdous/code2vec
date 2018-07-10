                    int start = disposition.indexOf("filename");

                    start = disposition.indexOf("=", start);

                    int end = disposition.indexOf(";", start);

                    if (end > start) {

                        fileName = disposition.substring(start + 1, end).trim();

                    } else {

                        fileName = disposition.substring(start + 1).trim();

                    }

                    if (fileName.startsWith("\"")) {

                        fileName = fileName.substring(1);

                    }

                    if (fileName.endsWith("\"")) {

                        fileName = fileName.substring(0, fileName.length() - 1);
