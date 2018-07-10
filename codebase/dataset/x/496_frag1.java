            ObjectInputStream.GetField fields = in.readFields();

            anInstanceVar = Integer.parseInt((String) fields.get(SIMULATED_FIELD_NAME, "-5"));

        }



        private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException, ClassNotFoundException {

            ObjectOutputStream.PutField fields = out.putFields();

            fields.put(SIMULATED_FIELD_NAME, Integer.toString(anInstanceVar));
