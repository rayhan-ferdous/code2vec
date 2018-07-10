                public void run() {

                    DataInputStream stdout = new DataInputStream(tac.getInputStream());

                    try {

                        for (int x = 0; x < 10000; x++) {

                            for (int i = 0; i < testData.length; i++) {

                                String in = stdout.readUTF();

                                charsRead += in.length();

                                if (!in.equals(testData[i])) throw new Error("TestRuntimeExec FAILED: bad input " + in);

                            }

                        }

                        int exitCode = tac.waitFor();

                        if (exitCode == 0 && charsRead == charsExpected && charsWritten == charsExpected) System.err.println("TestRuntimeExec SUCCESS"); else System.err.println("TestRuntimeExec FAILED");

                    } catch (Throwable e) {

                        e.printStackTrace();

                        throw new Error("TestRuntimeExec FAILED");

                    }

                }
