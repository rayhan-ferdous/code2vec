    public void publish(Element cruisecontrolLog) throws CruiseControlException {

        XMLLogHelper helper = new XMLLogHelper(cruisecontrolLog);

        boolean important = failAsImportant && !helper.isBuildSuccessful();

        Set userSet = new HashSet();

        Set alertSet = createAlertUserSet(helper);

        String subject = createSubject(helper);

        if (!alertSet.isEmpty()) {

            String alertSubject = "[MOD ALERT] " + subject;

            sendMail(createEmailString(alertSet), alertSubject, createMessage(helper), important);

        }

        if (shouldSend(helper)) {

            userSet.addAll(createUserSet(helper));

            userSet.removeAll(alertSet);

            if (!userSet.isEmpty()) {

                sendMail(createEmailString(userSet), subject, createMessage(helper), important);

            } else {

                if (alertSet.isEmpty()) {

                    LOG.info("No recipients, so not sending email");

                }

            }

        }

    }
