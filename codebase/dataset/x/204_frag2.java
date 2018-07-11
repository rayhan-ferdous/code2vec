    private static void processHeadings(org.jdom.Document jdomDoc) {

        final Pattern headingPattern = Pattern.compile("[Hh][123456]");

        class HeadingFilter implements Filter {



            public boolean matches(Object obj) {

                if (!(obj instanceof Element)) return false;

                Element el = (Element) obj;

                return headingPattern.matcher(el.getName()).matches();

            }

        }

        List<Element> toAddParaAfter = new ArrayList<Element>();

        for (Iterator hIt = jdomDoc.getDescendants(new HeadingFilter()); hIt.hasNext(); ) {

            Element hEl = (Element) hIt.next();

            Content next = getNextSibling(hEl);

            boolean emptyPara = false;

            if (next instanceof Element) {

                Element nextEl = (Element) next;

                if (nextEl.getName().equalsIgnoreCase("p") && nextEl.getChildren().isEmpty()) {

                    emptyPara = true;

                }

            }

            if (!emptyPara) {

                toAddParaAfter.add(hEl);

            }

        }

        for (Element hEl : toAddParaAfter) {

            Element parEl = hEl.getParentElement();

            int hIndex = parEl.indexOf(hEl);

            parEl.addContent(hIndex + 1, new Element("p"));

        }

    }
