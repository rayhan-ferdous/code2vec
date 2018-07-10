    public void getResponse(HTTPurl urlData, OutputStream outStream, HashMap headers) throws Exception {

        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(showAutoAddList(urlData));

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {

            outStream.write(remAutoAddItem(urlData));

            return;

        } else if ("03".equals(urlData.getParameter("action"))) {
