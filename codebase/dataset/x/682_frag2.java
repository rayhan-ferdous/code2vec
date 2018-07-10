                System.err.println("Simple buffered read did not read the full amount: 0x" + Integer.toHexString(totRead));

            }

            for (int i = 0x00; i < totRead; i++) {

                if (cbuf[i] != i) {

                    System.err.println("Error: 0x" + i + " read as 0x" + cbuf[i]);

                }

            }

            in.close();

            in = new StraightStreamReader(new FileInputStream(f));

            totRead = 0;

            while (totRead <= 0x100 && (read = in.read(cbuf, totRead, 0x100 - totRead)) > 0) {
