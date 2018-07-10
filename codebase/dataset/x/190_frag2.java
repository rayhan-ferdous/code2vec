    private void prepareRenderer(SQLRenderer r, String id) {

        r.add("applicant_id");

        r.add("password");

        r.add("applicant_name");

        r.add("address1");

        r.add("address2");

        r.add("address3");

        r.add("city");

        r.add("state");

        r.add("poscode");

        r.add("country_code");

        r.add("email");

        r.add("phone");

        r.add("gender");

        r.add("birth_date");

        r.add("apply_date");

        r.add("ip_address");

        if (!"".equals(id)) r.add("applicant_id", id);

    }
