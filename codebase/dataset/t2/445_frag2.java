    public void eUnset(int featureID) {

        switch(featureID) {

            case ActionstepPackage.WAIT_FOR_DIGIT__CALL1:

                setCall1((SafiCall) null);

                return;

            case ActionstepPackage.WAIT_FOR_DIGIT__TIMEOUT:

                setTimeout(TIMEOUT_EDEFAULT);

                return;

            case ActionstepPackage.WAIT_FOR_DIGIT__ACCEPTED_DIGITS:

                setAcceptedDigits(ACCEPTED_DIGITS_EDEFAULT);

                return;

        }

        super.eUnset(featureID);

    }
