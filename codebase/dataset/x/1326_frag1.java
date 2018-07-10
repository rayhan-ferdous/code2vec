    public static void compile(HibernateCompileContext ctx) throws CompileException {

        try {

            HibernateEntityCompile.transaction = ctx.manager.getModule();

            compileModule(ctx, ctx.manager.getModule(), true);

        } catch (Exception e) {

            StringBuffer sb = new StringBuffer();

            sb.append("transaction:\t").append(transaction.getName());

            if (module != null) sb.append("\r\nmodule:\t").append(module.getName());

            if (rule != null) {

                sb.append("\r\nrule:\t").append(rule.getType().getName()).append(" ").append(rule.getName()).append("(order ").append(rule.getOrder()).append(")");

            }

            if (text != null) sb.append("\r\nlast codetext:").append(text);

            if (e instanceof CompileException) throw new CompileException(sb.append("\r\n\r\n").append(e.getMessage()).toString());

            throw new CompileException(sb.append("\r\n\r\n").append(e.getMessage()).toString(), e);

        }

    }
