    public NotificationChain basicSetType(DynamicValue newType, NotificationChain msgs) {

        DynamicValue oldType = type;

        type = newType;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.GET_CALL_INFO__TYPE, oldType, newType);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }
