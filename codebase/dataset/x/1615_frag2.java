        for (int i = 0; i < nl1.getLength(); i++) {

            Node nd1 = nl1.item(i);

            if (filenameBuf.length() > 0) filenameBuf.append("-");

            filenameBuf.append(getNodeTextValue(nd1));

        }

        String outputFileName = outDir + File.separator + filenameBuf.toString() + ".htm";
