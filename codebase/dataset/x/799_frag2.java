            if (location.startsWith("file")) {

                java.io.File file = new java.io.File(url.getFile());

                return file.getAbsolutePath();

            } else {

                return url.toString();

            }

        } catch (Throwable t) {
