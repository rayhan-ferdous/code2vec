            robot = new Robot();

        } catch (AWTException awte) {

            awte.printStackTrace();

        }

    }



    private void initUI() {

        JFrame frame = new JFrame("Window Image");

        Container contents = frame.getContentPane();

        contents.setLayout(new BorderLayout());

        JButton button = new JButton("Take picture");
