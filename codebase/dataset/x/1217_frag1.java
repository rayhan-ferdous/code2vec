    @Override

    public void executeQuery() throws TerminalException {

        setUp();

        telnet.setPromptWaitingTimeOut(100);

        telnet.determinePrompt();

        String prompt = telnet.getPrompt();

        for (Entry<String, String> entry : this.queryResponseMap.entrySet()) {

            String queryCommand = entry.getKey();

            try {

                String response = telnet.send(queryCommand);

                int index1 = response.indexOf(queryCommand);

                if (index1 == -1) {

                    throw new TerminalException("terminal text process ERROR!");

                }

                int index2 = response.indexOf("\r\n", index1);

                int index3 = response.indexOf(prompt, index2);

                if (index3 == -1) {

                    throw new TerminalException("terminal text process ERROR!");

                }

                int index4 = response.lastIndexOf("\r\n");

                if (index4 > index3) {

                    throw new TerminalException("terminal text process ERROR!");

                }

                String cleanresponse;

                if (index2 == index4) {

                    cleanresponse = "";

                } else {

                    cleanresponse = response.substring(index2 + 2, index4);

                }

                entry.setValue(cleanresponse);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        tearDown();

    }
