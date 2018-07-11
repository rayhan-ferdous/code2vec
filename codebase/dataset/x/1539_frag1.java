    public void run() {

        while (executing) {

            try {

                Document doc = this.receiveDocument();

                LinkedList<Percept> percepts = evaluateDocument(doc);

                listener.handleMessage(this, percepts);

            } catch (IOException e) {

                LinkedList<Percept> percepts = new LinkedList<Percept>();

                percepts.add(new Percept("connectionLost"));

                listener.handleMessage(this, percepts);

                executing = false;

            }

        }

        try {

            this.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
