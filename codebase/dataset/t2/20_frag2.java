        int lastIndex = 0;

        int index = source.indexOf(whatBefore, lastIndex);

        while (index >= 0) {

            result.append(source.substring(lastIndex, index));

            result.append(whatAfter);

            lastIndex = index + beforeLen;

            index = source.indexOf(whatBefore, lastIndex);

        }
