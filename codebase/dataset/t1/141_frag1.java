        super((JFrame) plot.getFigurePanel().getGraphicalViewer());

        add(new ParetoRadiusPanel());

        setSize(365, 255);

        this.plot = plot;

        if (pdens != null) {

            pdens = plot.getParetoDensity();

            initialPercentileValue = pdens.getPercentile();

            initialNumberOfClusters = pdens.getClusters();

            initialRadius = pdens.getRadius();

        }

        addComponentListener(this);

    }



    public static void main(String[] args) {

        SphereRadiusDialog testapp = new SphereRadiusDialog(null);

        testapp.setVisible(true);

        testapp.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    }



    private javax.swing.JButton jButton1;



    private javax.swing.JButton jButton2;



    private javax.swing.JButton jButton3;



    private javax.swing.JLabel jLabel1;



    private javax.swing.JLabel jLabel2;



    private javax.swing.JLabel jLabel3;



    private javax.swing.JSeparator jSeparator1;



    private javax.swing.JSlider jSlider1;



    private javax.swing.JSpinner jSpinner1;



    private javax.swing.JTextField jTextField1;



    private JFormattedTextField jTextField2;



    /**

 * Panel as Inner class 

 */

    class ParetoRadiusPanel extends javax.swing.JPanel {



        /** Creates new form ParetoRadiusPanel */

        public ParetoRadiusPanel() {

            initComponents();

        }



        private void initComponents() {

            jButton1 = new javax.swing.JButton();

            jButton2 = new javax.swing.JButton();

            jButton3 = new javax.swing.JButton();

            jLabel1 = new javax.swing.JLabel();

            jSpinner1 = new javax.swing.JSpinner(new SpinnerNumberModel(initialNumberOfClusters, 1, 40, 1));

            jSlider1 = new javax.swing.JSlider(javax.swing.JSlider.HORIZONTAL, 0, 99, initialPercentileValue);

            jLabel2 = new javax.swing.JLabel();

            jTextField1 = new javax.swing.JTextField();

            jLabel3 = new javax.swing.JLabel();

            java.text.NumberFormat numberFormat = java.text.NumberFormat.getNumberInstance();

            NumberFormatter formatter = new NumberFormatter(numberFormat);

            jTextField2 = new JFormattedTextField(formatter);

            jSeparator1 = new javax.swing.JSeparator();

            setLayout(null);

            jButton1.setText("Close");

            add(jButton1);

            jButton1.setBounds(30, 190, 70, 25);

            jButton1.addActionListener(new ActionListener() {



                public void actionPerformed(ActionEvent arg0) {

                    dispose();

                }

            });

            jButton2.setText("Determine ParetoRadius");

            add(jButton2);

            jButton2.setBounds(112, 70, 218, 20);

            jButton2.addActionListener(new DetermineParetoRadiusAction());

            jButton3.setText("Apply");

            add(jButton3);

            jButton3.setBounds(250, 190, 70, 25);

            jButton3.addActionListener(new ApplyAction());

            jLabel1.setText("Number of expected Clusters :");

            add(jLabel1);

            jLabel1.setBounds(20, 20, 200, 15);

            add(jSpinner1);

            jSpinner1.setBounds(240, 20, 40, 20);

            add(jSlider1);

            jSlider1.setBounds(20, 140, 200, 16);

            jSlider1.addChangeListener(new SliderChangeListener());
