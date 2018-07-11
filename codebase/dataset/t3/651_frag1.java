    public void getResponse(HTTPurl urlData, OutputStream outStream) throws Exception {

        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(showCalendar(urlData));

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {

            outStream.write(showAddForm(urlData));

            return;

        } else if ("03".equals(urlData.getParameter("action"))) {
