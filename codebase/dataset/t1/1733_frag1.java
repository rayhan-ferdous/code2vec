    public void exitReaderAccount() {

        org.omg.CORBA.portable.InputStream $in = null;

        try {

            org.omg.CORBA.portable.OutputStream $out = _request("exitReaderAccount", true);

            $in = _invoke($out);

            return;

        } catch (org.omg.CORBA.portable.ApplicationException $ex) {

            $in = $ex.getInputStream();

            String _id = $ex.getId();

            throw new org.omg.CORBA.MARSHAL(_id);

        } catch (org.omg.CORBA.portable.RemarshalException $rm) {

            exitReaderAccount();

        } finally {

            _releaseReply($in);

        }

    }
