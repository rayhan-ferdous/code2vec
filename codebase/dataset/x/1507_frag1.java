    @Override

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {

        if (p instanceof LayoutParams) {

            LayoutParams result = new LayoutParams((LayoutParams) p);

            if (result.gravity <= Gravity.NO_GRAVITY) {

                result.gravity = Gravity.CENTER_VERTICAL;

            }

            return result;

        }

        return generateDefaultLayoutParams();

    }
