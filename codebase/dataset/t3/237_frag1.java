        Address[] a;

        if ((a = m.getFrom()) != null) {

            for (int j = 0; j < a.length; j++) System.out.println("FROM: " + a[j].toString());

        }

        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {

            for (int j = 0; j < a.length; j++) System.out.println("TO: " + a[j].toString());

        }

        System.out.println("SUBJECT: " + m.getSubject());

        Date d = m.getSentDate();

        System.out.println("SendDate: " + (d != null ? d.toString() : "UNKNOWN"));
