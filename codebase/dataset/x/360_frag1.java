        Map<String, List<String>> headerFields = uc.getHeaderFields();

        if (headerFields.containsKey("Set-Cookie")) {

            List<String> header = headerFields.get("Set-Cookie");

            for (int i = 0; i < header.size(); i++) {

                tmp = header.get(i);

                if (tmp.contains("SID")) {

                    sidcookie = tmp;

                }

                if (tmp.contains("ssui")) {
