            throw new JThinkRuntimeException(e);

        } finally {

            if (bufferedoutputstream != null) {

                try {

                    bufferedoutputstream.close();

                } catch (IOException e1) {

                    logger.error("关闭BufferedOutputStream时发生异常.", e1);

                    e1.printStackTrace();

                }

            }

            if (fileIn != null) {
