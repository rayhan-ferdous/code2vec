    public static final Class getClassByName(String name, boolean inThrow) {

        Class retClass = null;

        if (name.equals(Boolean.TYPE.getName())) {

            retClass = Boolean.TYPE;

        } else if (name.equals(Byte.TYPE.getName())) {

            retClass = Byte.TYPE;

        } else if (name.equals(Short.TYPE.getName())) {

            retClass = Short.TYPE;

        } else if (name.equals(Character.TYPE.getName())) {

            retClass = Character.TYPE;

        } else if (name.equals(Integer.TYPE.getName())) {

            retClass = Integer.TYPE;

        } else if (name.equals(Long.TYPE.getName())) {

            retClass = Long.TYPE;

        } else if (name.equals(Float.TYPE.getName())) {

            retClass = Float.TYPE;

        } else if (name.equals(Double.TYPE.getName())) {

            retClass = Double.TYPE;

        } else if (name.equals(Void.TYPE.getName())) {

            retClass = Void.TYPE;

        } else {

            try {

                retClass = Class.forName(name);

            } catch (ClassNotFoundException cnfe) {

                if (inThrow) {

                    throw new PFRuntimeException(cnfe);

                } else {

                    retClass = null;

                }

            }

        }

        return retClass;

    }
