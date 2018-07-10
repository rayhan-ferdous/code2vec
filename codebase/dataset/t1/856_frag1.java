    public void proceso() {

        try {

            abreConeccion();

            conex = new ServerSocket(PUERTO);

            log.info("--> TRACE: CreaServer:run(): Atendiendo pedidos en el puerto " + PUERTO);

            while (espero) {

                cliente = conex.accept();

                espero = false;

                error("CreaServer:run(): Llego el request");

            }

            b = new byte[longitudArchivo];

            String contentType = "liculape/rebebaira";

            outCliente = cliente.getOutputStream();

            PrintStream outStream = new PrintStream(outCliente);

            outStream.print("HTTP/1.0 200 OK\r\n");

            Date now = new Date();

            outStream.print("Date: " + now + "\r\n");

            outStream.print("Server: [lm2a] Server 1.0\r\n");

            outStream.print("Content-length: " + longitudArchivo + "\r\n");

            outStream.print("Content-type: " + contentType + "\r\n\r\n");

        } catch (IOException e) {

            error("abrirServerSocket(): ERROR #RC6--> Problemas al abrir conexiones");

            e.printStackTrace();

        }

        try {

            leeDesde(0);

        } catch (CoverlessException cle) {

            try {

                pausarCliente();

            } catch (IllegalStateException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();

            }

            setConexion(false);

            httpURLConnection = null;

            longitudArchivo = longitudArchivo - offset;

            error("proceso(): Error--> Palmo cuando intente leer la posicion " + offset + " bytes");

            try {

                archivoInputStream.close();

            } catch (IOException e) {

                log.info("proceso(): Excepcion al hacer el close");

                e.printStackTrace();

            }

            tryConnection(offset);

            cle.printStackTrace();

        }

    }
