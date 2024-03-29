    public void run() {

        try {

            Worker.parseFriends(this, sme, obj);

        } catch (IOException e) {

            System.err.println(e.getMessage());

        }

        sme.addResource(obj);

        synchronized (sme) {

            sme.shutdownThread(this);

            sme.notifyAll();

        }

    }
