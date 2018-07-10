    public NotificationChain basicSetCallerIdNameVar(DynamicValue newCallerIdNameVar, NotificationChain msgs) {

        DynamicValue oldCallerIdNameVar = callerIdNameVar;

        callerIdNameVar = newCallerIdNameVar;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__CALLER_ID_NAME_VAR, oldCallerIdNameVar, newCallerIdNameVar);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }
