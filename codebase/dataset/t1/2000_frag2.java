            while (rs.next()) {

                cli = new Cliente();

                cli.setId(rs.getInt("cod_cliente"));

                cli.setNif(rs.getString("nif"));

                cli.setNombre(rs.getString("nombre"));

                cli.setApellido1(rs.getString("apellido1"));

                cli.setApellido2(rs.getString("apellido2"));

                cli.setDireccion(rs.getString("direccion"));

                cli.setPoblacion(rs.getString("poblacion"));

                cli.setTelefono(rs.getString("telefono"));

                cli.setMovil(rs.getString("movil"));

                cli.setEmail(rs.getString("email"));
