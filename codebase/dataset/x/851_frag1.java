    public Object read(Object object) {

        try {

            return readMethod.invoke(object);

        } catch (Exception e) {

            throw new NanoRuntimeException(e);

        }

    }
