    public void addConstraint(final Constraint constraint) {

        constraints.add(constraint);

        final Scope scope = Scope.findScope(constraint.getScope(), scopes);

        if (scope == null) {

            scopes.add(new Scope(constraint));

        } else {

            scope.addConstraint(constraint);

        }

        if (!(constraint instanceof GlobalConstraint)) {

            relationManager.linkRelation(constraint.getRelation(), constraint);

        }

    }
