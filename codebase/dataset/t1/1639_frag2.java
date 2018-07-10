                cli.setTarjetaCredito(rs.getString("tarjeta_credito"));

                cli.setPuntos(rs.getInt("puntos"));

                System.out.println("Cargados datos de persona y cliente");

            } else {

                System.out.println("cliente " + id + " no encontrado.");

            }

            stmt.close();

            rs.close();

        } catch (SQLException e) {

            throw new errorSQL(e.toString());

        }

        return cli;

    }



    public int addCliente(Cliente cli) throws errorConexionBD, errorSQL {

        System.out.println("GestorCliente.addCliente()");

        String sql;

        PreparedStatement pstmt = null;

        Statement stmt = null;

        ResultSet rs = null;

        int nuevoId = 0;

        try {

            sql = "SELECT nif FROM persona WHERE nif='" + cli.getNif() + "'";

            System.out.println("Ejecuando: " + sql);

            stmt = gd.getConexion().createStatement();

            rs = stmt.executeQuery(sql);

            if (rs.next()) {

                throw new errorSQL("NIF duplicado en INSERT");

            }

            gd.begin();

            sql = "INSERT INTO persona(nif, nombre, apellido1, apellido2, " + "direccion, poblacion,telefono, movil, email, fecha_baja)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

            System.out.println("Ejecuando: " + sql);

            pstmt = gd.getConexion().prepareStatement(sql);

            pstmt.setString(1, cli.getNif());

            pstmt.setString(2, cli.getNombre());

            pstmt.setString(3, cli.getApellido1());

            pstmt.setString(4, cli.getApellido2());

            pstmt.setString(5, cli.getDireccion());

            pstmt.setString(6, cli.getPoblacion());

            pstmt.setString(7, cli.getTelefono());

            pstmt.setString(8, cli.getMovil());

            pstmt.setString(9, cli.getEmail());

            pstmt.setDate(10, (java.sql.Date) cli.getFechaBaja());

            rs = pstmt.executeQuery();

            System.out.println("Devolviendo el nuevo ID");

            if (rs.next()) {

                nuevoId = rs.getInt(1);

                System.out.println("Nuevo id: " + nuevoId);

            }

            pstmt.close();

            rs.close();

            sql = "INSERT INTO cliente (cod_cliente, tarjeta_credito, puntos)" + "VALUES (?,?,?) RETURNING cod_cliente";

            System.out.println("Ejecuando: " + sql);

            pstmt = gd.getConexion().prepareStatement(sql);

            pstmt.setInt(1, nuevoId);

            pstmt.setString(2, cli.getTarjetaCredito());

            pstmt.setInt(3, 0);

            gd.commit();

            System.out.println("commit");

            rs = pstmt.executeQuery();

            rs.close();

            pstmt.close();

            return nuevoId;

        } catch (SQLException e) {

            System.err.println("Error en GestorCliente.addCliente()");

            gd.rollback();

            throw new errorSQL(e.toString());
