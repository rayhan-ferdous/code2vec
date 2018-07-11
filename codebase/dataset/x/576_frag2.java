    public static void validate(String email, ValidationContext context) {

        if (email != null) {

            try {

                InternetAddress ia = new InternetAddress(email, true);

                ia.validate();

            } catch (AddressException aex) {

                context.addError(aex.getMessage() + ": " + email);

            }

        }

    }
