    private void displayNeedInfoForm(String filename, File file, boolean isTemplate, int reason, Element e) {

        super.writeHeader();

        String title = resources.getString("Enter_File_Information_Title");

        String message;

        out.print("<html><head><title>" + title + "</title></head>\n" + "<body><h1>" + title + "</h1>\n");

        if (file != null && reason != CREATE_CONFIRM) {

            message = resources.format("Missing_File_Message_FMT", HTMLUtils.escapeEntities(file.getPath()), new Integer(isTemplate ? 1 : 0));

            out.println(message);

            out.print("<P>");

        }

        String filenameDisplayName = HTMLUtils.escapeEntities(getProp(e, filename + DISPLAY_NAME_PROP, filename));

        message = resources.format("Provide_Info_Prompt_FMT", filenameDisplayName, new Integer(isTemplate ? 1 : 0), new Integer(reason));

        out.println(message);

        out.print("<form method='POST' action='");

        out.print((String) env.get("SCRIPT_PATH"));

        out.println("'><table>");

        for (int i = 0; i < pathVariableNames.size(); i++) {

            String varName = (String) pathVariableNames.get(i);

            PathVariable pathVar = getPathVariable(varName);

            if (pathVar.getDataname() == null) continue;

            pathVar.lookupExtraInfo(e);

            out.print("<tr><td valign='top'>");

            String displayName = pathVar.getDisplayName();

            if (!XMLUtils.hasValue(displayName)) displayName = varName;

            if (displayName.startsWith("/")) displayName = displayName.substring(1);

            out.print(TinyWebServer.encodeHtmlEntities(displayName));

            out.print("&nbsp;</td><td valign='top'>" + "<input size='40' type='text' name='");

            out.print(TinyWebServer.encodeHtmlEntities(varName));

            String value = pathVar.getValue();

            if (value != null) {

                out.print("' value='");

                out.print(TinyWebServer.encodeHtmlEntities(value));

            }

            out.print("'>");

            String comment = pathVar.getCommentText();

            if (XMLUtils.hasValue(comment)) {

                out.print("<br><i>");

                out.print(comment);

                out.print("</i><br>&nbsp;");

            }

            out.println("</td></tr>");

        }

        out.println("</table>");

        if (!(isTemplate == false && reason == MISSING_META)) out.print("<input type='hidden' name='" + CONFIRM_PARAM + "' " + "value='1'>\n");

        String pageCount = getParameter(PAGE_COUNT_PARAM);

        pageCount = (pageCount == null ? "x" : pageCount + "x");

        out.print("<input type='hidden' name='" + PAGE_COUNT_PARAM + "' value='");

        out.print(pageCount);

        out.print("'>\n" + "<input type='hidden' name='" + FILE_PARAM + "' value='");

        out.print(TinyWebServer.encodeHtmlEntities(filename));

        out.print("'>\n" + "<input type='submit' name='OK' value='OK'>\n" + "</form></body></html>\n");

    }
