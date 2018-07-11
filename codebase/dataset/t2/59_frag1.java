    public static String buildRequestPrefix(GenericDelegator delegator, Locale locale, String webSiteId, String https) {

        Map prefixValues = FastMap.newInstance();

        String prefix;

        NotificationServices.setBaseUrl(delegator, webSiteId, prefixValues);

        if (https != null && https.equalsIgnoreCase("true")) {

            prefix = (String) prefixValues.get("baseSecureUrl");

        } else {

            prefix = (String) prefixValues.get("baseUrl");

        }

        if (UtilValidate.isEmpty(prefix)) {

            if (https != null && https.equalsIgnoreCase("true")) {

                prefix = UtilProperties.getMessage("content", "baseSecureUrl", locale);

            } else {

                prefix = UtilProperties.getMessage("content", "baseUrl", locale);

            }

        }

        return prefix;

    }
