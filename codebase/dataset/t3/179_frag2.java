                hiword = VM_Magic.getMemoryWord(addr);

                addr += 4;

                long doubleBits = (((long) hiword) << 32) | (loword & 0xFFFFFFFFL);

                argObjectArray[i] = VM_Reflection.wrapDouble(Double.longBitsToDouble(doubleBits));

            } else if (argTypes[i].isLongType()) {
