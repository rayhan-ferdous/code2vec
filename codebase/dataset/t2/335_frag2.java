    public boolean equals(Object otherObj) {

        if (!(otherObj instanceof AbstractShortList)) {

            return false;

        }

        if (this == otherObj) return true;

        if (otherObj == null) return false;

        AbstractShortList other = (AbstractShortList) otherObj;

        if (size() != other.size()) return false;

        for (int i = size(); --i >= 0; ) {

            if (getQuick(i) != other.getQuick(i)) return false;

        }

        return true;

    }
