    private void loadData() {

        JTextField connNameTextField = getConnNameTextfield();

        readProfiles();

        if (profileList.isEmpty()) {

            connNameTextField.setText("Connection 1");

            connNameTextField.selectAll();

        } else {

            model = new MyComboBoxModel(profileList);

            connNameCombo.setModel(model);

            boolean preferenceLoaded = false;

            if (view != null) {

                String pref = view.getDBProfileNamePref();

                int index = this.findProfile(pref);

                if (index >= 0) {

                    loadUI(profileList.get(index));

                    preferenceLoaded = true;

                }

            }

            if (!preferenceLoaded) loadUI(profileList.get(0));

            setDialogState(LOADED);

        }

    }
