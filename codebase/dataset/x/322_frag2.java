            } else if (ch == '"') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;

                out.append(QUOTE_ENCODE);

            } else if (ch == '>') {

                if (i > last) {

                    out.append(input, last, i - last);

                }

                last = i + 1;

                out.append(GT_ENCODE);

            }

        }

        if (last == 0) {

            return string;

        }

        if (i > last) {

            out.append(input, last, i - last);

        }

        return out.toString();

    }



    /**

	 * Unescapes the String by converting XML escape sequences back into normal

	 * characters.

	 *

	 * @param string

	 *            the string to unescape.

	 * @return the string with appropriate characters unescaped.

	 */

    public static final String unescapeFromXML(String string) {

        string = replace(string, "&lt;", "<");

        string = replace(string, "&gt;", ">");

        string = replace(string, "&quot;", "\"");

        return replace(string, "&amp;", "&");

    }



    private static final char[] zeroArray = "0000000000000000".toCharArray();



    /**

	 * Pads the supplied String with 0's to the specified length and returns the

	 * result as a new String. For example, if the initial String is "9999" and

	 * the desired length is 8, the result would be "00009999". This type of

	 * padding is useful for creating numerical values that need to be stored

	 * and sorted as character data. Note: the current implementation of this

	 * method allows for a maximum <tt>length</tt> of 16.

	 *
