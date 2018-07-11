        public void writeExternal(ObjectOutput output) {

            try {

                output.writeUTF(value);

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
