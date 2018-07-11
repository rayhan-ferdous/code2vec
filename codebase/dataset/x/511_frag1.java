        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {

            return "ISO-10646-UCS-4";

        }

        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {

            return "ISO-10646-UCS-4";

        }

        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {

            return "ISO-10646-UCS-4";

        }

        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
