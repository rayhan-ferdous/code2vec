        Class wrapperType = Array.get(source, 0).getClass();

        Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);

        for (int i = 0; i < length; i++) {

            newArray[i] = Array.get(source, i);
