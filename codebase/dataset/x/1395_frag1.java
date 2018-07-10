        public int flush(Buffer buffer) throws IOException {

            if (random.nextInt(10) < 2) {

                return 0;

            }

            int len = random.nextInt(20 * rate);

            if (len > buffer.length()) len = buffer.length();

            Buffer temp = buffer.get(len);

            System.err.print(temp);

            return len;

        }
