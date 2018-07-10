            String recipient = basicUser.getEmail();

            if (recipient != null) {

                if (log.isDebugEnabled()) log.debug("delivery To:" + basicUser.getName() + " Sub:" + subject);

                try {

                    SimpleSMTPHeader header = getHeader(recipient, subject);

                    SMTPClient client = new SMTPClient();

                    client.connect(server);
