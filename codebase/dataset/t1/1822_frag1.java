    private void inlineMakeObject(String rType, String rTypeObject) {

        _insert("\n");

        if (!rType.equals(rTypeObject)) {

            _insert("    ");

            _insert(rTypeObject);

            _insert(" object_");

            _insert(rType);

            _insert(" = new ");

            _insert(rTypeObject);

            _insert("( value_");

            _insert(rType);

            _insert(" );\n");

        } else {

            _insert("    ");

            _insert(rTypeObject);

            _insert(" object_");

            _insert(rType);

            _insert(" = value_");

            _insert(rType);

            _insert(";\n");

        }

    }
