        if ((fileName == null) || (fileName.length() == 0)) {

            throw new IllegalArgumentException("File name cannot be null or empty.");

        }

        out.writeBytes("--");

        out.writeBytes(boundary);

        out.writeBytes("\r\n");

        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"");

        out.writeBytes("\r\n");

        if (mimeType != null) {

            out.writeBytes("Content-Type: " + mimeType);

            out.writeBytes("\r\n");

        }

        out.writeBytes("\r\n");

        out.write(data, 0, data.length);

        out.writeBytes("\r\n");
