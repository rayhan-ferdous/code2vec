        pw.println("                               .Q(write_nextgray[7]));   ");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     write_nextgray_8_inst.INIT = 1'b1;");

        pw.println("   // synopsys translate_on");

        pw.println("   // synthesis attribute INIT of write_nextgray_8_inst is \"1\"");

        pw.println("   FDSE write_nextgray_8_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .S(write_reset_in),");

        pw.println("                               .D(write_addr[8]),");

        pw.println("                               .Q(write_nextgray[8]));   ");

        pw.println("   ");

        pw.println("   ");

        pw.println("   // write_addrgray initializes to 9'b100000001");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     write_addrgray_0_inst.INIT = 1'b1;");

        pw.println("   // synopsys translate_on");

        pw.println("   // synthesis attribute INIT of write_addrgray_0_inst is \"1\"");

        pw.println("   FDSE write_addrgray_0_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .S(write_reset_in),");

        pw.println("                               .D(write_nextgray[0]),");

        pw.println("                               .Q(write_addrgray[0]));   ");

        pw.println("   ");

        pw.println("   FDRE write_addrgray_1_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_nextgray[1]),");
