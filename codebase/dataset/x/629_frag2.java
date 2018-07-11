            Statement stmt = db.getStatement();

            boolean found = false;

            {

                sql = "SELECT module_id FROM module_htmlcontainer WHERE module_id = '" + module_id + "' ";

                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) found = true;

            }

            if (found) sql = "UPDATE module_htmlcontainer SET html_url = '" + html_location + "' WHERE module_id = '" + module_id + "' "; else sql = "INSERT INTO module_htmlcontainer (module_id, html_url) VALUES ('" + module_id + "', '" + html_location + "')";

            stmt.executeUpdate(sql);
