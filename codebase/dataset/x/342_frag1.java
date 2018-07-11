        public void run() {

            try {

                device.write(address, data);

                barrier.await();

            } catch (Exception e) {

                e.printStackTrace();

            }

        }
