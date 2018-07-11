    public static void main(String[] args) {

        FetchMARC fm = new FetchMARC();

        try {

            FileReader fr = new FileReader("Agri.txt");

            BufferedReader buf = new BufferedReader(fr);

            String pmid = "";

            while ((pmid = buf.readLine()) != null) {

                pmid = pmid.trim();

                fm.fetchFile(pmid);

            }

            fm.writeMarc();

        } catch (Exception e) {

            System.out.println("Couldn't open stream");

            System.out.println(e);

        }

    }
