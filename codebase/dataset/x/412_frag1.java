        public void enqueue(BufferUpdateEvent e) {

            try {

                q.put(e);

            } catch (InterruptedException ex) {

            }

        }
