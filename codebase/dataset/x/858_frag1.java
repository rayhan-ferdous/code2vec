    @SuppressWarnings("unchecked")

    protected <T> Delegate<T> transferDelegateId(Input input, Output output, int fieldNumber) throws IOException {

        final int id = input.readUInt32();

        final RegisteredDelegate<T> rd = id < delegates.size() ? (RegisteredDelegate<T>) delegates.get(id) : null;

        if (rd == null) throw new UnknownTypeException("delegate id: " + id + " (Outdated registry)");

        output.writeUInt32(fieldNumber, id, false);

        return rd.delegate;

    }
