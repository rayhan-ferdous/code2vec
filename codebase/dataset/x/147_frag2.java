        } else if ("ISDN".equalsIgnoreCase(type)) {

            typedValue.setRel(com.google.gdata.data.extensions.PhoneNumber.Rel.ISDN);

        } else if ("TELEX".equalsIgnoreCase(type)) {

            typedValue.setRel(com.google.gdata.data.extensions.PhoneNumber.Rel.TELEX);

        } else if ("OTHER".equalsIgnoreCase(type)) {

            typedValue.setRel(com.google.gdata.data.extensions.PhoneNumber.Rel.OTHER);

        } else if ("MAIN".equalsIgnoreCase(type)) {

            typedValue.setRel(com.google.gdata.data.extensions.PhoneNumber.Rel.MAIN);

        }

        typedValue.setPhoneNumber(value);
