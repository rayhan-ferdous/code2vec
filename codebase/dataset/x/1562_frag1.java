        pw.println("   ");

        pw.println("   FDR write_truegray_1_inst (.C(write_clock),");

        pw.println("                              .R(write_reset_in),");

        pw.println("                              .D(write_addr[2] ^ write_addr[1]),");

        pw.println("                              .Q(write_truegray[1]));   ");

        pw.println("   ");

        pw.println("   FDR write_truegray_2_inst (.C(write_clock),");

        pw.println("                              .R(write_reset_in),");

        pw.println("                              .D(write_addr[3] ^ write_addr[2]),");

        pw.println("                              .Q(write_truegray[2]));   ");

        pw.println("   ");

        pw.println("   FDR write_truegray_3_inst (.C(write_clock),");

        pw.println("                              .R(write_reset_in),");

        pw.println("                              .D(write_addr[3]),");
