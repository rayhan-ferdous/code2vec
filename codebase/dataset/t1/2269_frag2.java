    public static <T extends Enum<?>> T generateRandomEnumFromClass(Class<T> c) {

        T[] enums = c.getEnumConstants();

        ArrayList<T> enumList = new ArrayList<T>();

        for (T enumObject : enums) {

            enumList.add(enumObject);

        }

        int choice = generator.nextInt(enumList.size());

        T enumToReturn = enumList.get(choice);

        return enumToReturn;

    }
