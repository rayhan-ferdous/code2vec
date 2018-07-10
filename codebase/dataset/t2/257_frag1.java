        ops[0x211] = new OP() {



            public final int call() {

                final short rm = Fetchb();

                if (rm >= 0xc0) {

                    Reg r = Modrm.GetEArd[rm];

                    r.dword = ADCD(Modrm.Getrd[rm].dword, r.dword);

                } else {

                    int eaa = getEaa(rm);

                    Memory.mem_writed(eaa, ADCD(Modrm.Getrd[rm].dword, Memory.mem_readd(eaa)));

                }

                return HANDLED;

            }

        };

        ops[0x213] = new OP() {



            public final int call() {

                final short rm = Fetchb();

                Reg r = Modrm.Getrd[rm];

                if (rm >= 0xc0) {

                    r.dword = ADCD(Modrm.GetEArd[rm].dword, r.dword);

                } else {

                    r.dword = ADCD(Memory.mem_readd(getEaa(rm)), r.dword);

                }

                return HANDLED;

            }

        };

        ops[0x215] = new OP() {
