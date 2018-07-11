    byte[] doAXFR(Name name, Message query, TSIG tsig, TSIGRecord qtsig, Socket s) {

        Zone zone = (Zone) znames.get(name);

        boolean first = true;

        if (zone == null) return errorMessage(query, Rcode.REFUSED);

        Iterator it = zone.AXFR();

        try {

            DataOutputStream dataOut;

            dataOut = new DataOutputStream(s.getOutputStream());

            int id = query.getHeader().getID();

            while (it.hasNext()) {

                RRset rrset = (RRset) it.next();

                Message response = new Message(id);

                Header header = response.getHeader();

                header.setFlag(Flags.QR);

                header.setFlag(Flags.AA);

                addRRset(rrset.getName(), response, rrset, Section.ANSWER, FLAG_DNSSECOK);

                if (tsig != null) {

                    tsig.applyStream(response, qtsig, first);

                    qtsig = response.getTSIG();

                }

                first = false;

                byte[] out = response.toWire();

                dataOut.writeShort(out.length);

                dataOut.write(out);

            }

        } catch (IOException ex) {

            System.out.println("AXFR failed");

        }

        try {

            s.close();

        } catch (IOException ex) {

        }

        return null;

    }
