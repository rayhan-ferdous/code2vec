    protected void closeStream() {

        try {

            if (m_audioInputStream != null) {

                m_audioInputStream.close();

                log.info("Stream closed");

            }

        } catch (IOException e) {

            log.info("Cannot close stream", e);

        }

    }
