        public boolean matches(Object o) {

            boolean success = false;

            if (o instanceof Element) {

                Element element = (Element) o;

                if (name.equals(element.getAttributeValue("name"))) {

                    success = true;

                }

            }

            return success;

        }
