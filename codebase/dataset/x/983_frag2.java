    public FragmentTransaction show(Fragment fragment) {

        if (fragment.mImmediateActivity == null) {

            throw new IllegalStateException("Fragment not added: " + fragment);

        }

        Op op = new Op();

        op.cmd = OP_SHOW;

        op.fragment = fragment;

        addOp(op);

        return this;

    }
