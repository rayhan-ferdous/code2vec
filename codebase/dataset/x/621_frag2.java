    public String getDisplayValueCardIdLink() {

        String displaValue = getDisplayValue();

        if (displaValue.length() > 0) {

            return "<a href='javascript:void(0)' onclick='openListTab" + getId() + "();return false;'>" + getDisplayValue() + "</a>";

        } else {

            return "";

        }

    }
