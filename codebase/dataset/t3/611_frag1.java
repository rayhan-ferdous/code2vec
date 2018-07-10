        public PathVariable(String name, String metaName, String impliedPath, String defaultValue) {

            this.metaName = metaName;

            SaveableData val = null;

            DataRepository data = getDataRepository();

            if (name.startsWith("/")) {

                val = data.getSimpleValue(dataName = name);

            } else {

                StringBuffer prefix = new StringBuffer(getPrefix());

                val = data.getInheritableValue(prefix, name);

                if (val != null && !(val instanceof SimpleData)) val = val.getSimpleValue();

                dataName = data.createDataName(prefix.toString(), name);

            }

            String postedValue = getParameter(name);

            if (postedValue != null) {

                value = postedValue;

                if (pathStartsWith(value, impliedPath)) {

                    value = value.substring(impliedPath.length());

                    if (value.startsWith(File.separator)) value = value.substring(1);

                }

                if (!pathEqual(value, defaultValue)) {

                    data.putValue(dataName, StringData.create(value));

                }

            } else if (val instanceof SimpleData) value = ((SimpleData) val).format();

            if (isUnknown() && defaultValue != null) value = defaultValue;

        }
