            @Override

            public void run() {

                try {

                    sendFileTransferRequest(createPeer(transfer));

                } catch (IOException e) {

                }

            }
