    static class StreamReader extends Thread {



        private InputStream is;



        private StringWriter sw;



        StreamReader(InputStream is) {

            this.is = is;

            sw = new StringWriter();

        }



        public void run() {

            try {

                int c;

                while ((c = is.read()) != -1) sw.write(c);

            } catch (IOException e) {

                ;

            }

        }



        String getResult() {

            return sw.toString();

        }

    }

}



/**

 * 

 * <p>

 * Title: YasperUI

 * </p>

 * 

 * <p>

 * Description: Dialog to obtain the available options.

 * </p>

 * 

 * <p>

 * Copyright: Copyright (c) 2004

 * </p>

 * 

 * <p>

 * Company:

 * </p>

 * 

 * @author Eric Verbeek

 * @version 1.0

 */

class YasperUI extends JDialog implements ActionListener {
