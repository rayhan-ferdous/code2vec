        } catch (RuntimeException e) {

            throw new DeleteDirectoryEntryFailure(e, this);

        }

    }



    public void moveTo(LgDirectory i_targetDirectory) throws MoveDirectoryEntryFailure, OperationNotSupportedExc {

        logger.info("moving " + this.getAbsoluteName() + " to " + i_targetDirectory.getAbsoluteName());

        java.io.File mappedLocalDirectory = this._getMappedLocalDirectory();

        try {

            if (mappedLocalDirectory.exists() == false) {

                throw new NoSuchDirectoryEntryExc(this.getParentDirectory(), this.getName());
