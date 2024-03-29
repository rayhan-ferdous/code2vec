    public NotificationChain basicSetFilename(DynamicValue newFilename, NotificationChain msgs) {

        DynamicValue oldFilename = filename;

        filename = newFilename;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.STREAM_AUDIO_EXTENDED__FILENAME, oldFilename, newFilename);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }
