    public DeviceData command_inout_reply(DeviceProxy deviceProxy, AsyncCallObject aco, int timeout) throws DevFailed, AsynReplyNotArrived {

        DeviceData argout = null;

        int ms_to_sleep = 50;

        AsynReplyNotArrived except = null;

        long t0 = System.currentTimeMillis();

        long t1 = t0;

        while (((t1 - t0) < timeout || timeout == 0) && argout == null) {

            try {

                argout = command_inout_reply(deviceProxy, aco);

            } catch (AsynReplyNotArrived na) {

                except = na;

                this.sleep(ms_to_sleep);

                t1 = System.currentTimeMillis();

            } catch (DevFailed e) {

                throw e;

            }

        }

        if (argout == null && except != null) throw except;

        return argout;

    }
