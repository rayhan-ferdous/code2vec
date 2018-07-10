    public String _align(String rText, String rBackText, int rColWidth, char rAlignment) {

        String result = rText;

        if (null == rText) {

            result = "";

        } else if (null != rBackText) {

            try {

                int textLen = rText.length();

                if (rColWidth > textLen) {

                    int backTextLen = rBackText.length();

                    int remainWidth = rColWidth - textLen;

                    int backTextRepeats = remainWidth / backTextLen;

                    int backTextRemain = remainWidth % backTextLen;

                    String back = "";

                    for (int backTextI = 0; backTextI < backTextRepeats; backTextI++) {

                        back = back + rBackText;

                    }

                    back = back + rBackText.substring(0, backTextRemain);

                    switch(rAlignment) {

                        case 'l':

                            result = result + back;

                            break;

                        case 'c':

                            result = back.substring(0, (back.length() / 2)) + result + back.substring((back.length() / 2));

                            break;

                        case 'r':

                            result = back + result;

                            break;

                    }

                }

            } catch (Exception e) {

                result = rText;

            }

        }

        return result;

    }
