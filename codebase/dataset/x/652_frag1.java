    private void writeRequest(final HttpURLConnection connection, final String message) {

        PrintWriter out = null;

        try {

            out = new PrintWriter(connection.getOutputStream());

            out.write(message);

            out.flush();

        } catch (Exception e) {

            log.error("Error sending data to the client", e);

            throw new RuntimeException("Unable to send message", e);

        } finally {

            if (out != null) {

                out.close();

            }

        }

    }
