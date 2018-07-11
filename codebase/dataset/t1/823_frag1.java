        private void emitEmitCall(String opcode, int[] args, int count, int level, int size) {

            emitTab(level);

            emit("emit" + opcode);

            for (int i = 0; i < count; i++) emit("_" + encoding[args[i]]);

            if (size != 0) emit("_" + encoding[size]);

            if (count == 0) emit("();\n"); else {

                emit("(");

                for (int i = 0; i < count; i++) {

                    emit("\n");

                    emitTab(level + 1);

                    emitArgs(i, args[i]);

                    if (i == count - 1) emit(");\n"); else emit(",");

                }

            }

        }
