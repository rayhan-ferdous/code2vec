    private void displayNeedInfoForm(String filename, File file, boolean isTemplate, int reason, Element e) {

        out.print("Content-type: text/html\r\n\r\n" + "<html><head><title>Enter File Information</title></head>\n" + "<body><h1>Enter File Information</h1>\n");

        if (file != null && reason != CREATE_CONFIRM) {

            out.print("The dashboard tried to find the ");

            out.print(isTemplate ? "<b>template</b>" : "file");

            out.print(" in the following location: <PRE>        ");

            out.print(file.getPath());

            out.println("</PRE>but no such file exists.<P>");

        }

        out.print("Please provide the following information to ");

        out.print(reason == CREATE_CONFIRM ? "create" : "help locate");

        out.print(" the '");

        out.print(filename);

        out.println(isTemplate ? "' template." : "'.");

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
