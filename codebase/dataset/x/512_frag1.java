        if (log) System.out.println(method + " '" + uri + "' ");

        Enumeration e = header.propertyNames();

        while (e.hasMoreElements()) {

            String value = (String) e.nextElement();

            if (log) System.out.println("  HDR: '" + value + "' = '" + header.getProperty(value) + "'");
