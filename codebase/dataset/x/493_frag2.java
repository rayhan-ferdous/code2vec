        if (test.equals("all") || test.equals("exampleXMLFilter")) {

            System.out.println("\n\n==== exampleXMLFilter ====");

            try {

                exampleXMLFilter(foo_xml, foo_xsl);

            } catch (Exception ex) {

                handleException(ex);

            }

        }

        if (test.equals("all") || test.equals("exampleXMLFilterChain")) {
