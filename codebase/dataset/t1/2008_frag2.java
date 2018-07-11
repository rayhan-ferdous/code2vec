        pw.println("                                read_xorout[0],");

        pw.println("                                read_xorout[1], ");

        pw.println("                                (read_xorout[1] ^ wag_readsync[3]),");

        pw.println("                                (read_xorout[1] ^ wag_readsync[3] ^ wag_readsync[2]),");

        pw.println("                                (read_xorout[0] ^ read_xorout[2]),");

        pw.println("                                (read_xorout[0] ^ read_xorout[2] ^ wag_readsync[0])");

        pw.println("                                };");

        pw.println("   ");

        pw.println("   FDR read_addrr_0_inst (.C(read_clock),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr[0]),");

        pw.println("                          .Q(read_addrr[0]));   ");

        pw.println("   ");

        pw.println("   FDR read_addrr_1_inst (.C(read_clock),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr[1]),");

        pw.println("                          .Q(read_addrr[1]));   ");

        pw.println("   ");

        pw.println("   FDR read_addrr_2_inst (.C(read_clock),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr[2]),");

        pw.println("                          .Q(read_addrr[2]));   ");

        pw.println("   ");

        pw.println("   FDR read_addrr_3_inst (.C(read_clock),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr[3]),");

        pw.println("                          .Q(read_addrr[3]));   ");

        pw.println("   ");

        pw.println("   FDR read_addrr_4_inst (.C(read_clock),");

        pw.println("                          .R(read_reset_in),");

        pw.println("                          .D(read_addr[4]),");
