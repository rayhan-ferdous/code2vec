        private static Set<Integer> getLinkageGroups(File filesDir, String fileName) {

            Set<Integer> result = new HashSet<Integer>();

            File genotypesFile = new File(filesDir, fileName);

            SAXReader reader = new SAXReader();

            Document document = null;

            try {

                document = reader.read(genotypesFile);

            } catch (DocumentException ex) {

                throw new RuntimeException(ex);

            }

            Element root = document.getRootElement();

            for (Object obj : root.elements("ch")) {

                Element element = (Element) obj;

                Integer parsedLinkagGroup = Integer.valueOf(element.attribute("id").getText());

                result.add(parsedLinkagGroup);

            }

            return result;

        }
