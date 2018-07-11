    public UserDocument getUser(String userParams) {

        ResultSet rsLogin = null;

        GetUserDocument in = null;

        UserDocument out = null;

        try {

            helper = new DBHelper();

            in = GetUserDocument.Factory.parse(userParams);

            String sql = null;

            if (in.getGetUser().getUserParams().getUsingUsername()) {

                sql = SQL.getLoginDetail();

            } else {

                sql = SQL.getUserDetail();

            }

            out = UserDocument.Factory.newInstance();

            out.addNewUser();

            PreparedStatement psLogin = helper.prepareStatement(sql);

            psLogin.setString(1, in.getGetUser().getUserParams().getParam());

            rsLogin = psLogin.executeQuery();

            if (rsLogin.next()) {

                String pswd = rsLogin.getString("PASSWORD");

                String role = rsLogin.getString("ROLE");

                String userno = rsLogin.getString("USERNO");

                String title = rsLogin.getString("TITLE");

                String name = rsLogin.getString("NAME");

                String surname = rsLogin.getString("SURNAME");

                String telephone = rsLogin.getString("TELEPHONE");

                String cellphone = rsLogin.getString("CELLPHONE");

                out.getUser().setCellphone(cellphone);

                out.getUser().setLoggedIn(true);

                out.getUser().setRole(role);

                out.getUser().setUserno(userno);

                out.getUser().setTitle(title);

                out.getUser().setName(name);

                out.getUser().setSurname(surname);

                out.getUser().setTelephone(telephone);

                out.getUser().setUsername(in.getGetUser().getUserParams().getParam());

            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                if (rsLogin != null) {

                    rsLogin.close();

                }

                if (helper != null) {

                    helper.cleanup();

                }

            } catch (SQLException e) {

            }

        }

        return out;

    }
