    @Override

    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {

        if (baseClass == CallConsumer1.class) {

            switch(derivedFeatureID) {

                case ActionstepPackage.CHAN_SPY__CALL1:

                    return CallPackage.CALL_CONSUMER1__CALL1;

                default:

                    return -1;

            }

        }

        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);

    }
