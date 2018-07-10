        process_output.append(cmd.getProcessOutputAsString());

        String driveLetter = getDriveLetter();

        if (driveLetter == null) {

            process_error.append("Could not assign drive letter to virtual floppy drive, all letters are in use!");

            return this.returnWithError(process_error.toString());

        }

        cmd.setCommand(linkDriveLetter(driveLetter));

        cmd.run();
