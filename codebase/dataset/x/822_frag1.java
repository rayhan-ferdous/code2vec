    private static void getData(String myurl) throws Exception {

        u = new URL(myurl);

        uc = (HttpURLConnection) u.openConnection();

        uc.setRequestProperty("Referer", "https://login.live.com/");

        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

        String k = "";

        while ((tmp = br.readLine()) != null) {

            k += tmp;

        }

    }
