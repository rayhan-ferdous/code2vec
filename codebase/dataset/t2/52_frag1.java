    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {

        if (baseClass == CallConsumer1.class) {

            switch(derivedFeatureID) {

                case ActionstepPackage.SET_CALLER_ID__CALL1:

                    return CallPackage.CALL_CONSUMER1__CALL1;

                default:

                    return -1;

            }

        }

        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);

    }
