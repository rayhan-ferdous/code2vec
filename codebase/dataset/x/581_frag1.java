    public void confirmIcV(JLabel status) {

        setToRead(false);

        _status = status;

        if (status != null) status.setText(java.text.MessageFormat.format(rbt.getString("StateConfirmIndexedCV"), new Object[] { "" + _iCv, "" + _piVal + (_siVal >= 0 ? "." + _siVal : "") }));

        if (mProgrammer != null) {

            setBusy(true);

            _reading = false;

            _confirm = true;

            try {

                setState(UNKNOWN);

                mProgrammer.readCV(_iCv, this);

            } catch (Exception e) {

                setState(UNKNOWN);

                if (status != null) status.setText(java.text.MessageFormat.format(rbt.getString("StateExceptionDuringIndexedRead"), new Object[] { e.toString() }));

                log.warn("Exception during IndexedCV read: " + e);

                setBusy(false);

            }

        } else {

            if (status != null) status.setText(rbt.getString("StateNoProgrammer"));

            log.error("No programmer available!");

        }

    }
