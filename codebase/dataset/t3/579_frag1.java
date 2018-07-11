import java.util.*;

import java.text.*;

import java.net.*;



class FileManagementDataRes extends HTTPResponse {



    public FileManagementDataRes() throws Exception {

        super();

    }



    public void getResponse(HTTPurl urlData, OutputStream outStream, HashMap<String, String> headers) throws Exception {

        if ("01".equals(urlData.getParameter("action"))) {

            outStream.write(deleteFile(urlData));

            return;

        } else if ("02".equals(urlData.getParameter("action"))) {

            outStream.write(showCommandList(urlData));

            return;

        } else if ("03".equals(urlData.getParameter("action"))) {

            outStream.write(runCommand(urlData));

            return;

        }
