    public static void toggleIgnoreCaseFor(View view) {

        DualDiff dualDiff = DualDiffManager.getDualDiffFor(view);

        if (dualDiff != null) {

            dualDiff.toggleIgnoreCase();

            dualDiff.refresh();

        } else {

            if (jEdit.getBooleanProperty(BEEP_ON_ERROR)) {

                view.getToolkit().beep();

            }

        }

    }
