    private static String findDescription(String head) {

        String description = null;

        try {

            Matcher m = P_DESCRIPTION.matcher(head);

            if (m.find()) {

                description = findContent(m.group());

            }

        } catch (Exception e) {

            log.error(e.getMessage(), e);

        }

        return description;

    }
