        rfw.setUserID("test");

        md5 = new MD5();

        System.out.println("md5.getMD5ofStr(\"test\") = " + md5.getMD5ofStr("test"));

        rfw.setPassWord(md5.getMD5ofStr("test"));

        rfw.setStartTime("2008-07-05");

        rfw.setEndTime("2010-09-08");

        rfw.setReceiverNumber("1111");

        ReceiveFaxResultWrapper rf = faxUtils.receiveFax(rfw);
