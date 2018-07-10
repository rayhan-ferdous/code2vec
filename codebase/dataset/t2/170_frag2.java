class ExeFileFilter extends javax.swing.filechooser.FileFilter {



    @Override

    public boolean accept(File f) {

        if (f.isDirectory()) {

            return true;

        }

        String extension = Utils.getExtension(f);

        if (extension != null) {

            if (extension.equals(Utils.exe)) {

                return true;

            } else {

                return false;

            }

        }

        return false;

    }



    @Override

    public String getDescription() {

        return "PCAS Script File";

    }

}



class Utils {
