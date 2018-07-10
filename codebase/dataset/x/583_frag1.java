    public static Map clonePropertiesForObject(Object anObject) {

        Object key;

        Map result = readPropertiesForObject(anObject);

        Iterator it = result.keySet().iterator();

        while (it.hasNext()) {

            key = it.next();

            result.put(key, deepClone(result.get(key)));

        }

        return result;

    }
