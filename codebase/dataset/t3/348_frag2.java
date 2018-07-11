        public <T> Field<T> create(int number, String name, final java.lang.reflect.Field f, IdStrategy strategy) {

            final Delegate<Object> delegate = strategy.getDelegate((Class<Object>) f.getType());

            return new Field<T>(FieldType.BYTES, number, name) {



                {

                    f.setAccessible(true);

                }



                public void mergeFrom(Input input, T message) throws IOException {

                    final Object value = delegate.readFrom(input);

                    try {

                        f.set(message, value);

                    } catch (IllegalArgumentException e) {

                        throw new RuntimeException(e);

                    } catch (IllegalAccessException e) {

                        throw new RuntimeException(e);

                    }

                }



                public void writeTo(Output output, T message) throws IOException {

                    final Object value;

                    try {

                        value = (Object) f.get(message);

                    } catch (IllegalArgumentException e) {

                        throw new RuntimeException(e);

                    } catch (IllegalAccessException e) {

                        throw new RuntimeException(e);

                    }

                    if (value != null) delegate.writeTo(output, number, value, false);

                }



                public void transfer(Pipe pipe, Input input, Output output, boolean repeated) throws IOException {

                    delegate.transfer(pipe, input, output, number, repeated);

                }

            };

        }
