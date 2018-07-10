        emittedOpcodes.add("LOCK");

        emitTab(3);

        emit("case IG_PATCH_POINT_opcode:\n");

        emitTab(4);

        emit("emitPatchPoint();\n");

        emitTab(4);

        emit("break;\n");

        Set errorOpcodes = getErrorOpcodes(emittedOpcodes);

        if (!errorOpcodes.isEmpty()) {

            i = errorOpcodes.iterator();

            while (i.hasNext()) {

                emitTab(3);

                emit("case IA32_" + i.next() + "_opcode:\n");

            }

            emitTab(4);

            emit("throw new OPT_OptimizingCompilerException(inst + \" has unimplemented IA32 opcode (check excludedOpcodes)\");\n");

        }

        emitTab(2);

        emit("}\n");

        emitTab(2);

        emit("inst.setmcOffset( mi );\n");

        emitTab(1);

        emit("}\n\n");

        emit("\n}\n");

        try {

            out.close();

        } catch (IOException e) {

            e.printStackTrace();

            System.exit(-1);

        }

    }
