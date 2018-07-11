            out.write("\r\n");

            out.write("<script src=\"./js/geral.js\" type=\"text/javascript\"></script>\r\n");

            out.write("<script src=\"./js/jQuery.js\" type=\"text/javascript\"></script>\r\n");

            out.write("<script src=\"./js/calendar.js\" type=\"text/javascript\"></script>\r\n");

            out.write("<script src=\"./js/jquery.alphanumeric.pack.js\" type=\"text/javascript\"></script>\r\n");

            out.write("<script src=\"./js/jquery.tablesorter.js\" type=\"text/javascript\"></script>\r\n");

            out.write("<script src=\"./js/linhaTabela.js\" type=\"text/javascript\"></script>\r\n");

            out.write("\r\n");

            out.write("<script>\r\n");

            out.write("\t\r\n");

            out.write("\tfunction carregarMascaras() {\r\n");

            out.write("\r\n");

            out.write("\t\tmesMask = new Mask(\"##\", \"number\");\r\n");

            out.write("\t\tmesMask.attach(document.getElementById('mesInicial'));\r\n");

            out.write("\t\tmesMask.attach(document.getElementById('mesFinal'));\r\n");

            out.write("\t\t\r\n");

            out.write("\t\tanoMask = new Mask(\"####\", \"number\");\r\n");

            out.write("\t\tanoMask.attach(document.getElementById('anoInicial'));\r\n");
