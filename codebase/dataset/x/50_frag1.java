                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {

                    String id = rs.getString("module_id");

                    userModules.addElement(id);

                }

            }

            Vector deletedModules = new Vector();

            for (int i = 0; i < userModules.size(); i++) {
