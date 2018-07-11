        response.reset();

        response.setBufferSize(DEFAULT_BUFFER_SIZE);

        response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");

        response.setHeader("Accept-Ranges", "bytes");

        response.setHeader("ETag", eTag);

        response.setDateHeader("Last-Modified", lastModified);

        response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

        RandomAccessFile input = null;

        OutputStream output = null;

        try {

            input = new RandomAccessFile(file, "r");

            output = response.getOutputStream();

            if (ranges.isEmpty() || ranges.get(0) == full) {

                Range r = full;

                response.setContentType(contentType);

                response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);

                if (content) {

                    if (acceptsGzip) {

                        response.setHeader("Content-Encoding", "gzip");

                        output = new GZIPOutputStream(output, DEFAULT_BUFFER_SIZE);
