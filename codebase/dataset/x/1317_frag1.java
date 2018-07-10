    public void setMPSStatusValidator(MPSStatusValidator mpsStatusValidator) {

        if (this.mpsStatusValidator != null) {

            this.mpsStatusValidator.removeChangeListener(mpsStatusValidatorParamChangeListener);

            mpsStatusValidateMeasurement = false;

        }

        this.mpsStatusValidator = mpsStatusValidator;

        if (mpsStatusValidator != null) {

            if (mpsStatusValidatorParamChangeListener == null) {

                mpsStatusValidatorParamChangeListener = new ChangeListener() {



                    public void stateChanged(ChangeEvent changeEvent) {

                        MPSStatusValidator mpsValidCntr = (MPSStatusValidator) changeEvent.getSource();

                        mpsStatusValidateMeasurement = mpsValidCntr.isOn();

                    }

                };

            }

            mpsStatusValidator.addChangeListener(mpsStatusValidatorParamChangeListener);

            mpsStatusValidateMeasurement = mpsStatusValidator.isOn();

        }

    }
