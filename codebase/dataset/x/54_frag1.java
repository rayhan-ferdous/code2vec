                out.write("\r\n");

                out.write("\t<script type=\"text/javascript\">\r\n");

                out.write("\tnew LiveValidation('birthday').add(Validate.Date).add(Validate.Presence);\r\n");

                out.write("\tnew LiveValidation('city').add(Validate.City).add(Validate.Presence);\r\n");

                out.write("\tnew LiveValidation('email').add(Validate.Email).add(Validate.Presence);\r\n");

                out.write("\tnew LiveValidation('firstName').add(Validate.Name).add(Validate.Presence);\r\n");

                out.write("\tnew LiveValidation('house_number').add(Validate.Housenumber).add(\r\n");

                out.write("\t\t\tValidate.Presence);\r\n");
