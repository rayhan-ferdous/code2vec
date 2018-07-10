                public void run() {

                    try {

                        finish();

                        ThreadedIndexWriter.super.rollback();

                    } catch (CorruptIndexException e) {

                        LOGGER.error("Error updating index.", e);

                    } catch (IOException e) {

                        LOGGER.error("Error updating index.", e);

                    }

                }
