import javax.swing.JPanel;



public class GuiTest {



    /**

     * @param args

     * @throws AWTException 

     * @throws IOException 

     * @throws InterruptedException 

     */

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {

        JFrame gui = new JFrame();

        JPanel panel = new JPanel();

        JButton button = new JButton();

        button.setText("print Screen");

        button.addActionListener(new PrintScreenActionListener());

        panel.add(button);

        gui.add(panel);
