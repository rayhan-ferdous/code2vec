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
