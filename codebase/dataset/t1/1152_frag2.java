import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;



public class Interactive extends JFrame implements JDValueListener {



    JDrawEditor theGraph;



    JDObject btn1;



    JDObject btn2;



    JDObject checkbox;



    JDLabel textArea;



    String[] lines = { "", "", "" };



    public Interactive() {

        theGraph = new JDrawEditor(JDrawEditor.MODE_PLAY);

        try {

            theGraph.loadFile("interactive.jdw");
