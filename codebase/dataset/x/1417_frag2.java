        try {

            URL url = new URL(_inputfile);

            BufferedReader inStream = new BufferedReader(new InputStreamReader(url.openStream()));

            _aux_values = new Vector();

            _aux_mass = new Vector();

            _aux_intensity = new Vector();

            _line_count = 0;
