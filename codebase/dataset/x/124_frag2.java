                for (int i = 0; i < buffers.length; i++) {

                    WritableCacheBuffer buf = (WritableCacheBuffer) buffers[i];

                    try {

                        buf.sync();

                    } catch (IOException e) {
