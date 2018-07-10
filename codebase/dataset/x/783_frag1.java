        for (int i = 0; i < lastIndex; ++i) {

            Dataset tmp = item.getItem(tagPath[i]);

            item = tmp != null ? tmp : item.putSQ(tagPath[i]).addNewItem();

        }

        if (val.length() != 0) item.putXX(tagPath[lastIndex], val); else if (!item.contains(tagPath[lastIndex])) item.putXX(tagPath[lastIndex]);
