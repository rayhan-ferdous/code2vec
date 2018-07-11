    protected void messageBoard(boolean post, RequestPacket pack) {

        {

            if (CommandThread.core == null) return;

            if (post == true) {

                m_requestQueue.add(pack);

                CommandThread.core.interrupt();

            } else {

                if (m_requestQueue.size() > 0) {

                    pack.set((RequestPacket) m_requestQueue.removeFirst());

                }

            }

        }

    }
