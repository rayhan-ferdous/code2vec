                if (resolve) return getCall1();

                return basicGetCall1();

            case ActionstepPackage.EXTENSION_SPY__EXTENSION:

                return getExtension();

            case ActionstepPackage.EXTENSION_SPY__CONTEXT:

                return getContext();

            case ActionstepPackage.EXTENSION_SPY__SPY_BRIDGED_ONLY:

                return isSpyBridgedOnly();

            case ActionstepPackage.EXTENSION_SPY__GROUP:
