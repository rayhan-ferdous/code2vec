        pw.println("                               .Q(write_nextgray[1]));   ");

        pw.println("   ");

        pw.println("   FDRE write_nextgray_2_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_addr[3] ^ write_addr[2]),");

        pw.println("                               .Q(write_nextgray[2]));   ");

        pw.println("   ");

        pw.println("   FDRE write_nextgray_3_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_addr[4] ^ write_addr[3]),");

        pw.println("                               .Q(write_nextgray[3]));   ");

        pw.println("   ");

        pw.println("   FDRE write_nextgray_4_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_addr[5] ^ write_addr[4]),");

        pw.println("                               .Q(write_nextgray[4]));   ");

        pw.println("   ");

        pw.println("   FDRE write_nextgray_5_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_addr[6] ^ write_addr[5]),");

        pw.println("                               .Q(write_nextgray[5]));   ");

        pw.println("   ");

        pw.println("   FDRE write_nextgray_6_inst (.C(write_clock),");

        pw.println("                               .CE(write_allow),");

        pw.println("                               .R(write_reset_in),");

        pw.println("                               .D(write_addr[7] ^ write_addr[6]),");

        pw.println("                               .Q(write_nextgray[6]));   ");
