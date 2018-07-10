                    public void run() {

                        while (!monitor.isCanceled()) {

                            try {

                                Thread.sleep(100);

                            } catch (final InterruptedException ex) {

                                return;

                            }

                        }

                        _isCancelImport = true;

                        while (currentThread.isAlive()) {

                            currentThread.interrupt();

                            try {

                                Thread.sleep(10);

                            } catch (final InterruptedException ex) {

                            }

                        }

                    }
