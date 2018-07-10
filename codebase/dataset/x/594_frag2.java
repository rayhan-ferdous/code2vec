        pw.println("                               .Q(write_addrgray[3]));      ");

        pw.println("   ");

        pw.println("   ");

        pw.println("   /************************************************************\\");

        pw.println("    *                                                            *");

        pw.println("    *  Alternative generation of FIFOstatus outputs.  Used to    *");

        pw.println("    *  determine how full FIFO is, based on how far the Write    *");

        pw.println("    *  pointer is ahead of the Read pointer.  read_truegray      *   ");

        pw.println("    *  is synchronized to write_clock (rag_writesync), converted *");

        pw.println("    *  to binary (ra_writesync), and then subtracted from the    *");

        pw.println("    *  pipelined write_addr (write_addrr) to find out how many   *");

        pw.println("    *  words are in the FIFO (fifostatus).  The top bits are     *   ");

        pw.println("    *  then 1/2 full, 1/4 full, etc. (not mutually exclusive).   *");

        pw.println("    *  fifostatus has a one-cycle latency on write_clock; or,    *");

        pw.println("    *  one cycle after the write address is incremented on a     *   ");
