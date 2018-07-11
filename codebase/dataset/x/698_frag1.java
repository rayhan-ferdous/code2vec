    private boolean initTCPSocketAndStartPacketReader() {

        InputStream in = null;

        try {

            _tcpSocket = new Socket(_host, _port);

            _tcpOut = _tcpSocket.getOutputStream();

            in = _tcpSocket.getInputStream();

        } catch (Exception e) {

            _logger.log(Level.SEVERE, e.getLocalizedMessage(), e);

            return false;

        }

        setState(State.OPENED_SOCKET);

        _tcpPacketReaderThread = new Thread(new TCPPacketReader(in, this));

        _tcpPacketReaderThread.start();

        return true;

    }
