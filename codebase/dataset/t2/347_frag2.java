    public NotificationChain basicSetContext(DynamicValue newContext, NotificationChain msgs) {

        DynamicValue oldContext = context;

        context = newContext;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.ORIGINATE_CALL__CONTEXT, oldContext, newContext);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }
