            Object o = msg.getContent();

            if (msg.isMimeType("text/plain")) {

                out.println("<pre>");

                out.println((String) o);

                out.println("</pre>");

            } else if (msg.isMimeType("multipart/*")) {

                Multipart mp = (Multipart) o;
