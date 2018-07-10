                Attribute att = at.getAttribute((String) key);

                Matcher m = null;

                try {

                    int type = att.getType();

                    if (type == Attribute.CONTAINER) {

                        Object val = getMetaData(att.getContainer());

                        result.put(att.getName(), val);
