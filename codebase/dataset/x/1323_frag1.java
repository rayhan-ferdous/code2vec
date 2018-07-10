    public static void main(String[] args) {

        Cliente c = new Cliente();

        try {

            GestorCliente gCli = new GestorCliente();

            try {

                c.setId(2);

                c.setNif("12345679");

                c.setNombre("Pepe");

                c.setApellido1("P�rez");

                c.setApellido2("G�mez");

                c.setDireccion("C/Saboya 12-3");

                c.setPoblacion("Barcelona");

                c.setTelefono("93666555");

                c.setMovil("666 001122");

                c.setEmail("un.correo@no.es");

                c.setCodUsuario("2");

                c.setTarjetaCredito("123456789");

                gCli.addCliente(c);

            } catch (errorSQL e) {

                System.out.println(e.getMessage());

            }

        } catch (errorConexionBD e) {

            System.out.println(e.getMessage());

        }

    }
