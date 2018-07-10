    public Command getBackCommand() {

        if (backCommand == null) {

            backCommand = new Command("Back", Command.BACK, 0);

        }

        return backCommand;

    }
