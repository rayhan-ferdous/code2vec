    private Directory indexDirectory() {

        if (_indexDirectory == null) {

            try {

                if (_store.startsWith("file://")) {

                    File indexDirectory = new File(new URL(_store).getFile());

                    _indexDirectory = FSDirectory.open(indexDirectory);

                } else {

                    EOEditingContext ec = ERXEC.newEditingContext();

                    ec.lock();

                    try {

                        _indexDirectory = ERIDirectory.clazz.directoryForName(ec, _store);

                    } finally {

                        ec.unlock();

                    }

                }

            } catch (IOException e) {

                throw NSForwardException._runtimeExceptionForThrowable(e);

            }

        }

        return _indexDirectory;

    }
