        @Override

        public int readUnsignedShort() {

            try {

                return input.readUnsignedShort();

            } catch (IOException e) {

                throw new IllegalStateException(e);

            }

        }
