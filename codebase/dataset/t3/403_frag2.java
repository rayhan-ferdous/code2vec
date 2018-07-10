        public OutputStream(java.io.OutputStream out, int options) {

            super(out);

            this.breakLines = (options & DONT_BREAK_LINES) != DONT_BREAK_LINES;

            this.encode = (options & ENCODE) == ENCODE;

            this.bufferLength = encode ? 3 : 4;

            this.buffer = new byte[bufferLength];

            this.position = 0;

            this.lineLength = 0;

            this.suspendEncoding = false;

            this.b4 = new byte[4];

            this.options = options;

            this.alphabet = getAlphabet(options);

            this.decodabet = getDecodabet(options);

        }
