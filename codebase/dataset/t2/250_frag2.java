    public static String rename_file(String sessionid, String key, String newFileName) {

        String jsonstring = "";

        try {

            Log.d("current running function name:", "rename_file");

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));

            nameValuePairs.add(new BasicNameValuePair("m", "rename_file"));

            nameValuePairs.add(new BasicNameValuePair("new_name", newFileName));

            nameValuePairs.add(new BasicNameValuePair("key", key));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);

            HttpResponse response = httpclient.execute(httppost);

            jsonstring = EntityUtils.toString(response.getEntity());

            Log.d("jsonStringReturned:", jsonstring);

            return jsonstring;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return jsonstring;

    }



    /**

	 * rename a specific tag

	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session

	 * @param originalTag specify the original tag with absolute path for you to rename

	 * @param newTagName the new tag name you want to set

	 */

    public static String rename_tag(String sessionid, String originalTag, String newTagName) {
