    public Response multPartURL(String fileParamName, String url, PostParameter[] params, File file, boolean authenticated) throws WeiboException {

        PostMethod post = new PostMethod(url);

        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();

        try {

            long t = System.currentTimeMillis();

            Part[] parts = null;

            if (params == null) {

                parts = new Part[1];

            } else {

                parts = new Part[params.length + 1];

            }

            if (params != null) {

                int i = 0;

                for (PostParameter entry : params) {

                    parts[i++] = new StringPart(entry.getName(), (String) entry.getValue());

                }

            }

            FilePart filePart = new FilePart(fileParamName, file.getName(), file, new MimetypesFileTypeMap().getContentType(file), "UTF-8");

            filePart.setTransferEncoding("binary");

            parts[parts.length - 1] = filePart;

            post.setRequestEntity(new MultipartRequestEntity(parts, post.getParams()));

            List<Header> headers = new ArrayList<Header>();

            if (authenticated) {

                if (basic == null && oauth == null) {

                }

                String authorization = null;

                if (null != oauth) {

                    authorization = oauth.generateAuthorizationHeader("POST", url, params, oauthToken);

                } else if (null != basic) {

                    authorization = this.basic;

                } else {

                    throw new IllegalStateException("Neither user ID/password combination nor OAuth consumer key/secret combination supplied");

                }

                headers.add(new Header("Authorization", authorization));

                log("Authorization: " + authorization);

            }

            client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

            client.executeMethod(post);

            Response response = new Response();

            response.setResponseAsString(post.getResponseBodyAsString());

            response.setStatusCode(post.getStatusCode());

            log("multPartURL URL:" + url + ", result:" + response + ", time:" + (System.currentTimeMillis() - t));

            return response;

        } catch (Exception ex) {

            throw new WeiboException(ex.getMessage(), ex, -1);

        } finally {

            post.releaseConnection();

            client = null;

        }

    }
