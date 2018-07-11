            if (parser.getAttributeName(i).equals("name")) {

                name = parser.getAttributeValue(i);

            }

        }

        if (name == null) {

            missingAttr(parser, "name");

        }

        parser.next();

        return name;

    }



    private static void missingAttr(XmlPullParser parser, String attr) throws XmlPullParserException {

        throw new XmlPullParserException("Missing \"" + attr + "\" attribute in \"" + parser.getName() + "\"-tag.");
