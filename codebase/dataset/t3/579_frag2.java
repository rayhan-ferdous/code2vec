import java.text.SimpleDateFormat;

import java.util.*;

import java.net.*;



class EpgAutoAddDataRes extends HTTPResponse {



    public EpgAutoAddDataRes() throws Exception {

        super();

    }



    public void getResponse(HTTPurl urlData, OutputStream outStream, HashMap<String, String> headers) throws Exception {

        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(showAutoAddList(urlData));

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {

            outStream.write(remAutoAddItem(urlData));

            return;

        } else if ("03".equals(urlData.getParameter("action"))) {

            outStream.write(showAutoEpgAddForm(urlData));

            return;

        } else if ("04".equals(urlData.getParameter("action"))) {
