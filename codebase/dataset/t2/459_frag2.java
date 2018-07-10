    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {

        if (baseClass == CallConsumer1.class) {

            switch(baseFeatureID) {

                case CallPackage.CALL_CONSUMER1__CALL1:

                    return ActionstepPackage.SAY_NUMBER__CALL1;

                default:

                    return -1;

            }

        }

        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);

    }
