    private void inlineObjectValue(String rType, String rTypeObject) {

        _insert("\n    ");

        _insert(rTypeObject);

        _insert(" object_");

        _insert(rType);

        _insert(" = (");

        _insert(rTypeObject);

        _insert(") object;\n");

        if (!rType.equals(rTypeObject)) {

            _insert("    ");

            _insert(rType);

            _insert(" value_");

            _insert(rType);

            _insert(" = object_");

            _insert(rType);

            _insert(".");

            _insert(rType);

            _insert("Value();\n");

        } else {

            _insert("    ");

            _insert(rType);

            _insert(" value_");

            _insert(rType);

            _insert(" = object_");

            _insert(rType);

            _insert(";\n");

        }

    }
