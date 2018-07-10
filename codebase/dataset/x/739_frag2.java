        pw.println("                          .CE(read_allow),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr_plus_one[7]),");

        pw.println("                          .Q(read_addr[7]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addr_8_inst (.C(read_clock),");

        pw.println("                          .CE(read_allow),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr_plus_one[8]),");

        pw.println("                          .Q(read_addr[8]));   ");

        pw.println("   ");

        pw.println("   ");

        pw.println("   // read_nextgray initializes to 9'b100000000");

        pw.println("   FDRE read_nextgray_0_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[1] ^ read_addr[0]),");

        pw.println("                              .Q(read_nextgray[0]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_1_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[2] ^ read_addr[1]),");

        pw.println("                              .Q(read_nextgray[1]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_2_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[3] ^ read_addr[2]),");

        pw.println("                              .Q(read_nextgray[2]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_3_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[4] ^ read_addr[3]),");

        pw.println("                              .Q(read_nextgray[3]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_4_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[5] ^ read_addr[4]),");

        pw.println("                              .Q(read_nextgray[4]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_5_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[6] ^ read_addr[5]),");

        pw.println("                              .Q(read_nextgray[5]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_6_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[7] ^ read_addr[6]),");

        pw.println("                              .Q(read_nextgray[6]));   ");

        pw.println("   ");

        pw.println("   FDRE read_nextgray_7_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_addr[8] ^ read_addr[7]),");

        pw.println("                              .Q(read_nextgray[7]));   ");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     read_nextgray_8_inst.INIT = 1'b1;");

        pw.println("   // synopsys translate_on");

        pw.println("   // synthesis attribute INIT of read_nextgray_8_inst is \"1\"");

        pw.println("   FDSE read_nextgray_8_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .S(read_reset_in),");

        pw.println("                              .D(read_addr[8]),");

        pw.println("                              .Q(read_nextgray[8]));   ");

        pw.println("   ");

        pw.println("   ");

        pw.println("   // read_addrgray initializes to 9'b100000001");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     read_addrgray_0_inst.INIT = 1'b1;");

        pw.println("   // synopsys translate_on");

        pw.println("   // synthesis attribute INIT of read_addrgray_0_inst is \"1\"");

        pw.println("   FDSE read_addrgray_0_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .S(read_reset_in),");

        pw.println("                              .D(read_nextgray[0]),");

        pw.println("                              .Q(read_addrgray[0]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_1_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[1]),");

        pw.println("                              .Q(read_addrgray[1]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_2_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[2]),");

        pw.println("                              .Q(read_addrgray[2]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_3_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[3]),");

        pw.println("                              .Q(read_addrgray[3]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_4_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[4]),");

        pw.println("                              .Q(read_addrgray[4]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_5_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[5]),");

        pw.println("                              .Q(read_addrgray[5]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_6_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[6]),");

        pw.println("                              .Q(read_addrgray[6]));   ");

        pw.println("   ");

        pw.println("   FDRE read_addrgray_7_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .R(read_reset_in),");

        pw.println("                              .D(read_nextgray[7]),");

        pw.println("                              .Q(read_addrgray[7]));   ");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     read_addrgray_8_inst.INIT = 1'b1;");

        pw.println("   // synopsys translate_on");

        pw.println("   // synthesis attribute INIT of read_addrgray_8_inst is \"1\"");

        pw.println("   FDSE read_addrgray_8_inst (.C(read_clock),");

        pw.println("                              .CE(read_allow),");

        pw.println("                              .S(read_reset_in),");

        pw.println("                              .D(read_nextgray[8]),");

        pw.println("                              .Q(read_addrgray[8]));   ");

        pw.println("   ");

        pw.println("   ");

        pw.println("   // read_lastgray initializes to 9'b100000011");

        pw.println("   ");

        pw.println("   // synopsys translate_off");

        pw.println("   defparam     read_lastgray_0_inst.INIT = 1'b1;");
