    void assignPropsValues() {

        Enumeration keys = allProps.keys();

        while (keys.hasMoreElements()) {

            String key = (String) keys.nextElement();

            SkinProperty p = (SkinProperty) allProps.get(key);

            String propValue = (String) romizedProps.get(key);

            if (propValue != null) {

                p.value = propValue;

            } else {

                if (!p.isNew) {

                    missingProps.add(p);
