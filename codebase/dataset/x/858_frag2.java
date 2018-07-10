    @SuppressWarnings("unchecked")

    protected <T> HasSchema<T> transferPojoId(Input input, Output output, int fieldNumber) throws IOException {

        final int id = input.readUInt32();

        final BaseHS<T> wrapper = (BaseHS<T>) pojos.get(id);

        if (wrapper == null) throw new UnknownTypeException("pojo id: " + id + " (Outdated registry)");

        output.writeUInt32(fieldNumber, id, false);

        return wrapper;

    }
