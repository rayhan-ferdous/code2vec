                if (size == Byte) for (int i = 0; i < count; i++) if (args[i] == Register) if (currentOpcode.indexOf("MOVZX") == -1 && currentOpcode.indexOf("MOVSX") == -1) {

                    emitTab(level);

                    emit("if (VM.VerifyAssertions && !(");

                    emitArgs(i, Register);

                    emit(" < 4)) VM._assert(false, inst.toString());\n");
