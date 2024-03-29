        if (c == java.lang.Object.class) {

            return new IDLType(c, new String[] { "java", "lang" }, "Object");

        } else if (c == java.lang.String.class) {

            return new IDLType(c, new String[] { "CORBA" }, "WStringValue");

        } else if (c == java.lang.Class.class) {

            return new IDLType(c, new String[] { "javax", "rmi", "CORBA" }, "ClassDesc");

        } else if (c == java.io.Serializable.class) {

            return new IDLType(c, new String[] { "java", "io" }, "Serializable");

        } else if (c == java.io.Externalizable.class) {

            return new IDLType(c, new String[] { "java", "io" }, "Externalizable");

        } else if (c == java.rmi.Remote.class) {
