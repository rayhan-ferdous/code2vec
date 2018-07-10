    public Interactive() {

        theGraph = new JDrawEditor(JDrawEditor.MODE_PLAY);

        try {

            theGraph.loadFile("interactive.jdw");

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this, e.getMessage(), "Error loading file", JOptionPane.ERROR_MESSAGE);

            System.exit(1);

        }

        btn1 = getObject("Button1");

        btn2 = getObject("Button2");

        checkbox = getObject("Checkbox");

        textArea = (JDLabel) getObject("textArea");

        addText("");

        btn1.addValueListener(this);

        btn2.addValueListener(this);

        checkbox.addValueListener(this);

        setContentPane(theGraph);

        setTitle("Interactive");

    }
