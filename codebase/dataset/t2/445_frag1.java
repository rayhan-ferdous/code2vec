    public void eUnset(int featureID) {

        switch(featureID) {

            case ActionstepPackage.FESTIVAL__CALL1:

                setCall1((SafiCall) null);

                return;

            case ActionstepPackage.FESTIVAL__TEXT:

                setText((DynamicValue) null);

                return;

            case ActionstepPackage.FESTIVAL__INTERRUPT_KEYS:

                setInterruptKeys(INTERRUPT_KEYS_EDEFAULT);

                return;

        }

        super.eUnset(featureID);

    }
