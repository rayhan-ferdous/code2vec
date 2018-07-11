    public void execute() {

        try {

            loadOpstring();

            replaceHeaderCategory();

            replaceCodeServerCategory();

            replaceGroupsCategory();

            storeOpstring();

        } catch (UnexpectedOpstringFormatException uofe) {

            logger.severe("Opstring transform failed due to:" + uofe);

        } catch (IOException ioe) {

            logger.severe("Opstring transform failed due to:" + ioe);

        }

    }
