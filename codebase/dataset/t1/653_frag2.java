            u = new URL("https://www.box.net/api/1.0/auth/" + ticket);

            uc = (HttpURLConnection) u.openConnection();

            br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            tmp = "";

            k = "";

            while ((tmp = br.readLine()) != null) {

                k += tmp;

            }

            request_token = CommonUploaderTasks.parseResponse(k, "request_token = '", "'");
