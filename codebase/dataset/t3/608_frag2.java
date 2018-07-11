    public void postCookies() {

        StringBuffer cookieList = new StringBuffer();

        for (Iterator<Entry<String, String>> i = cookies.entrySet().iterator(); i.hasNext(); ) {

            Entry<String, String> entry = (i.next());

            cookieList.append(entry.getKey().toString() + "=" + entry.getValue());

            if (i.hasNext()) {

                cookieList.append("; ");

            }

        }

        if (cookieList.length() > 0) {

            connection.setRequestProperty("Cookie", cookieList.toString());

        }

    }
