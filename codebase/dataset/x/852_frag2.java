        opcodes[0x91] = "STA ($%1), y";

        opcodes[0x94] = "STY $%1,x";

        opcodes[0x95] = "STA $%1,x";

        opcodes[0x96] = "STX $%1,y";

        opcodes[0x98] = "TYA";

        opcodes[0x99] = "STA $%2%1,y";

        opcodes[0x9A] = "TXS";

        opcodes[0x9D] = "STA $%2%1,x";

        opcodes[0xA0] = "LDY #$%1";

        opcodes[0xA1] = "LDA $(%2%1,x)";

        opcodes[0xA2] = "LDX #$%1";

        opcodes[0xA4] = "LDY $%1";

        opcodes[0xA5] = "LDA $%1";

        opcodes[0xA6] = "LDX $%1";

        opcodes[0xA8] = "TAY";

        opcodes[0xA9] = "LDA #$%1";

        opcodes[0xAA] = "TAX";

        opcodes[0xAC] = "LDY $%2%1";

        opcodes[0xAD] = "LDA $%2%1";

        opcodes[0xAE] = "LDX $%2%1";

        opcodes[0xB0] = "BCS $%3";

        opcodes[0xB1] = "LDA ($%1), y";

        opcodes[0xB4] = "LDY $%1,x";

        opcodes[0xB5] = "LDA $%1,x";

        opcodes[0xB6] = "LDX $%1,y";

        opcodes[0xB8] = "CLV";

        opcodes[0xB9] = "LDA $%2%1,y";

        opcodes[0xBA] = "TSX";

        opcodes[0xBC] = "LDY $%2%1,x";

        opcodes[0xBD] = "LDA $%2%1,x";

        opcodes[0xBE] = "LDX $%2%1,y";

        opcodes[0xC0] = "CPY #$%1";

        opcodes[0xC1] = "CMP $(%2%1,x)";

        opcodes[0xC4] = "CPY $%1";

        opcodes[0xC5] = "CMP $%1";

        opcodes[0xC6] = "DEC $%1";

        opcodes[0xC8] = "INY";

        opcodes[0xC9] = "CMP #$%1";

        opcodes[0xCA] = "DEX";

        opcodes[0xCC] = "CPY $%2%1";

        opcodes[0xCD] = "CMP $%2%1";

        opcodes[0xCE] = "DEC $%2%1";

        opcodes[0xD0] = "BNE $%3";

        opcodes[0xD1] = "CMP ($%1), y";

        opcodes[0xD5] = "CMP $%1,x";

        opcodes[0xD6] = "DEC $%1,x";

        opcodes[0xD8] = "CLD";

        opcodes[0xD9] = "CMP $%2%1,y";

        opcodes[0xDD] = "CMP $%2%1,x";

        opcodes[0xDE] = "DEC $%2%1,x";

        opcodes[0xE0] = "CPX #$%1";

        opcodes[0xE1] = "SBC $(%2%1,x)";

        opcodes[0xE4] = "CPX $%1";

        opcodes[0xE5] = "SBC $%1";

        opcodes[0xE6] = "INC $%1";

        opcodes[0xE8] = "INX";

        opcodes[0xE9] = "SBC #$%1";

        opcodes[0xEA] = "NOP";

        opcodes[0xEC] = "CPX $%2%1";

        opcodes[0xED] = "SBC $%2%1";

        opcodes[0xEE] = "INC $%2%1";

        opcodes[0xF0] = "BEQ $%3";

        opcodes[0xF1] = "SBC ($%1), y";

        opcodes[0xF5] = "SBC $%1,x";
