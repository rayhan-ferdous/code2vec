    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null) return false;

        if (getClass() != obj.getClass()) return false;

        Music other = (Music) obj;

        if (region != other.region) return false;

        if (expansion != other.expansion) return false;

        if (id != other.id) return false;

        return true;

    }
