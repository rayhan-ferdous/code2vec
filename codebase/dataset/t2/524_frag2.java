                        for (int j = 0; j < col2Width; j++) {

                            String delimiter = value.substring(valueLeftIndex + col2Width - j - 1, valueLeftIndex + col2Width - j);

                            if (delimiter.equals(" ") || delimiter.equals(";") || delimiter.equals(",")) {

                                valueRightIndex = valueLeftIndex + col2Width - j;

                                break;

                            }

                        }

                        trimmedValue = value.substring(valueLeftIndex, valueRightIndex);

                        valueLeftIndex = valueRightIndex;

                        s = s + "   " + trimmedValue;
