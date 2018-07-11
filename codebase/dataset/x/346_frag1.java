        if ("contactID".equals(pColumn)) return new Long(vo.getContactID());

        if ("name".equals(pColumn)) return vo.getName();

        if ("department".equals(pColumn)) return vo.getDepartment();
