        @Override

        public void doRead(DataInputStream in) throws Exception {

            for (int i = 0; i < maxID; i++) {

                users[i].futures = in.readInt();

            }

        }
