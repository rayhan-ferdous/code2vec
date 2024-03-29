    public static IDecisionRule selectNewRule(Component parent, ClassLoader classLoader) {

        ToxTreePackageEntries ruleTypes = Introspection.getAvailableRuleTypes(classLoader);

        Object name = selectFromList(parent, "Select a rule", "Available rules:", new ToxTreePackageEntryModel(ruleTypes), null);

        if ((name != null) && (name instanceof ToxTreePackageEntry)) try {

            Object o = Introspection.loadCreateObject(((ToxTreePackageEntry) name).getClassName());

            if (o instanceof IDecisionRule) return (IDecisionRule) o; else {

                o = null;

                return null;

            }

        } catch (Exception x) {

            x.printStackTrace();

            return null;

        } else return null;

    }
