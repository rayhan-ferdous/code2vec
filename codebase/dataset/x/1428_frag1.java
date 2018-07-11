    public void rename(String newName) {

        ObjectContainerBase container = container();

        if (!container.isClient()) {

            _name = newName;

            _containingClass.setStateDirty();

            _containingClass.write(container.systemTransaction());

        } else {

            Exceptions4.throwRuntimeException(58);

        }

    }
