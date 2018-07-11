                unfulfilledConstraints = KerberosUtil.INTEGRITY_REQUIRED_CONSTRAINTS;

            } else if (KerberosUtil.containsConstraint(constraints.preferences(), Integrity.YES)) {

                unfulfilledConstraints = KerberosUtil.INTEGRITY_PREFERRED_CONSTRAINTS;

            } else {

                unfulfilledConstraints = InvocationConstraints.EMPTY;

            }

            connectionAbsoluteTime = Math.min(computeConnectionTimeLimit(constraints.requirements()), computeConnectionTimeLimit(constraints.preferences()));
