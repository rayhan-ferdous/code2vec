        checkRange(from, size());

        java.util.Iterator e = other.iterator();

        int index = from;

        int limit = Math.min(size() - from, other.size());

        for (int i = 0; i < limit; i++) set(index++, ((Number) e.next()).intValue());
