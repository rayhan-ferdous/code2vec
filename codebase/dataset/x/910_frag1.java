    public List<String> getLogLines() throws IOException {

        List<String> _log = new ArrayList<String>();

        File _logFile = getListenerFile(this._repository, "log");

        if (_logFile.exists()) {

            LineNumberReader _lr = null;

            try {

                _lr = new LineNumberReader(new FileReader(_logFile));

                for (String _line = _lr.readLine(); _line != null; _line = _lr.readLine()) {

                    _log.add(_line);

                }

            } finally {

                if (_lr != null) {

                    _lr.close();

                }

            }

        }

        return _log;

    }
