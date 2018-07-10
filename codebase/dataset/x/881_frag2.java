    @Override

    public TransactionConfig clone() {

        try {

            return (TransactionConfig) super.clone();

        } catch (CloneNotSupportedException willNeverOccur) {

            return null;

        }

    }
