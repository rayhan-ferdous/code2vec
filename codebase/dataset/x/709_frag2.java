                                    ColorItem aux = getColorItem(rows, Double.parseDouble((String) parser.getAttributeValue(i)));

                                    if (aux != null) a = aux.getColor().getAlpha();

                                }

                                if (parser.getAttributeName(i).equals("name")) {
