                    if (item.getComments() != null) {

                        Text content = doc.createTextNode(item.getComments());

                        Element elem = doc.createElement("comments");

                        elem.appendChild(content);

                        itelem.appendChild(elem);

                    }

                    if (item.getEnclosure() != null) {

                        Item.Enclosure enclosure = item.getEnclosure();

                        Element elem = doc.createElement("enclosure");

                        elem.setAttribute("url", enclosure.getUrl());

                        elem.setAttribute("length", String.valueOf(enclosure.getLength()));

                        elem.setAttribute("type", enclosure.getType());

                        itelem.appendChild(elem);
