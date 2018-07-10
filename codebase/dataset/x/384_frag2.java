    public String toString() {

        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());

        result.append(" (timeout: ");

        result.append(timeout);

        result.append(", acceptedDigits: ");

        result.append(acceptedDigits);

        result.append(')');

        return result.toString();

    }
