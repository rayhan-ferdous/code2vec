            return false;

        }

        String sopClassUID = ds.getString(Tags.SOPClassUID);

        if (sopClassUID == null) {

            log.error(MessageFormat.format(messages.getString("noSOPclass"), new Object[] { file }));

            return false;
