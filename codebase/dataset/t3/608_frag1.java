    private void postCookies() {

        StringBuffer cookieList = new StringBuffer(_rawCookies);

        for (Iterator i = _cookies.entrySet().iterator(); i.hasNext(); ) {

            Map.Entry entry = (Map.Entry) (i.next());

            cookieList.append(entry.getKey().toString() + "=" + entry.getValue());

            if (i.hasNext()) {

                cookieList.append("; ");

            }

        }

        if (cookieList.length() > 0) {

            _connection.setRequestProperty("Cookie", cookieList.toString());

        }

    }
