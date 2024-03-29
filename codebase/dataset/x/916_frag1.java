        public void startElement(final String uri, final String localName, final String qName, final Attributes atts) throws SAXException {

            if (currentElement.getNodeType() != Node.ENTITY_REFERENCE_NODE) {

                CElement elem = new CElement(qName, document);

                for (int i = 0; i < atts.getLength(); i++) {

                    CAttr attr = new CAttr(atts.getQName(i), atts.getValue(i), document, currentElement, true);

                    if (currentElement.listAttributes == null) {

                        currentElement.listAttributes = new CNamedNodeMap(currentElement);

                    }

                    currentElement.listAttributes.setNamedItemForce(attr);

                }

                if (currentElement == document) {

                    document.appendChildInternal(elem);

                } else {

                    elem.parentNode = currentElement;

                    if (currentElement.listChild == null) {

                        currentElement.listChild = new CNodeList(false);

                    }

                    currentElement.listChild.addItem(elem);

                }

                currentElement = elem;

            }

        }
