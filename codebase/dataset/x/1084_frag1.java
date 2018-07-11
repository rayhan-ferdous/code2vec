    public List<SelectItem> CompanyContacts(Company co) {

        Contact c;

        ArrayList<SelectItem> outList = new ArrayList<SelectItem>(0);

        outList.add(new SelectItem(null, "---"));

        if (null != co) {

            Iterator<Contact> i = co.getContacts().iterator();

            while (i.hasNext()) {

                c = i.next();

                outList.add(new SelectItem(c, c.getName()));

            }

        }

        return outList;

    }
