        command.append("AND DISABLED<>'true' ORDER BY TRUNKNAME");

        TBLPBXTRUNK.fill(command);

        PrintWriter configFile = new PrintWriter(new FileWriter(configBase + "tegsoft_REGISTER.conf", false));

        for (int i = 0; i < TBLPBXTRUNK.getRowCount(); i++) {

            DataRow rowTBLPBXTRUNK = TBLPBXTRUNK.getRow(i);

            if (NullStatus.isNull(rowTBLPBXTRUNK.getString("REGISTER"))) {

                continue;

            }

            configFile.println("register => " + rowTBLPBXTRUNK.getString("REGISTER"));

            configFile.println();

            configFile.println();

        }

        configFile.close();
