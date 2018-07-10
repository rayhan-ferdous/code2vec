    public SafiCall getCall1() {

        if (call1 != null && call1.eIsProxy()) {

            InternalEObject oldCall1 = (InternalEObject) call1;

            call1 = (SafiCall) eResolveProxy(oldCall1);

            if (call1 != oldCall1) {

                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ActionstepPackage.PROGRESS__CALL1, oldCall1, call1));

            }

        }

        return call1;

    }
