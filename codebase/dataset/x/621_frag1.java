    public Boolean getBold() {

        ContentExt ext = getContentExt();

        if (ext != null) {

            return ext.getBold();

        } else {

            return null;

        }

    }
