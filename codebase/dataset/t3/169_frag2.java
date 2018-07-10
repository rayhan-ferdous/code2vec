    static Object[] packageParameterFromVarArg(VM_Method targetMethod, int argAddress) {

        VM_Type[] argTypes = targetMethod.getParameterTypes();

        int argCount = argTypes.length;

        Object[] argObjectArray = new Object[argCount];

        VM_JNIEnvironment env = VM_Thread.getCurrentThread().getJNIEnv();

        for (int i = 0, addr = argAddress; i < argCount; i++) {

            int loword, hiword;

            loword = VM_Magic.getMemoryWord(addr);

            addr += 4;

            if (argTypes[i].isFloatType()) {

                hiword = VM_Magic.getMemoryWord(addr);

                addr += 4;

                long doubleBits = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapFloat((float) (Double.longBitsToDouble(doubleBits)));

            } else if (argTypes[i].isDoubleType()) {

                hiword = VM_Magic.getMemoryWord(addr);

                addr += 4;

                long doubleBits = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapDouble(Double.longBitsToDouble(doubleBits));

            } else if (argTypes[i].isLongType()) {

                hiword = VM_Magic.getMemoryWord(addr);

                addr += 4;

                long longValue = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapLong(longValue);

            } else if (argTypes[i].isBooleanType()) {

                argObjectArray[i] = VM_Reflection.wrapBoolean(loword);

            } else if (argTypes[i].isByteType()) {

                argObjectArray[i] = VM_Reflection.wrapByte((byte) loword);

            } else if (argTypes[i].isCharType()) {

                argObjectArray[i] = VM_Reflection.wrapChar((char) loword);

            } else if (argTypes[i].isShortType()) {

                argObjectArray[i] = VM_Reflection.wrapShort((short) loword);

            } else if (argTypes[i].isReferenceType()) {

                argObjectArray[i] = env.getJNIRef(loword);

            } else if (argTypes[i].isIntType()) {

                argObjectArray[i] = VM_Reflection.wrapInt(loword);

            } else {

                return null;

            }

        }

        return argObjectArray;

    }
