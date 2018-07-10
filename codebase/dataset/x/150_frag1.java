                    public void run() {

                        try {

                            read(BufferUtils.getEmptyBuffer());

                        } catch (IOException iox) {

                            FileUtils.close(SSLReadWriteChannel.this);

                        }

                    }
