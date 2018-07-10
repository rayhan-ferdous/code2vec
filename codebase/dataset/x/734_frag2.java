                return existing;

            }

        }

        return m_constants.add(new CONSTANT_Utf8_info(value));

    }



    public int addStringConstant(final String value) {

        final int value_index = addCONSTANT_Utf8(value, true);

        return m_constants.add(new CONSTANT_String_info(value_index));

    }



    public int addNameType(final String name, final String typeDescriptor) {

        final int name_index = addCONSTANT_Utf8(name, true);
