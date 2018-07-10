        public void visitVarDef(JCVariableDecl tree) {

            tree.mods = translate(tree.mods);

            tree.vartype = translate(tree.vartype);

            if (tree.init != null) {

                if ((tree.mods.flags & (Flags.PUBLIC | Flags.PROTECTED)) != 0) tree.init = translate(tree.init); else {

                    String t = tree.vartype.toString();

                    if (t.equals("boolean")) tree.init = new JCLiteral(TypeTags.BOOLEAN, 0) {

                    }; else if (t.equals("byte")) tree.init = new JCLiteral(TypeTags.BYTE, 0) {

                    }; else if (t.equals("char")) tree.init = new JCLiteral(TypeTags.CHAR, 0) {

                    }; else if (t.equals("double")) tree.init = new JCLiteral(TypeTags.DOUBLE, 0.d) {

                    }; else if (t.equals("float")) tree.init = new JCLiteral(TypeTags.FLOAT, 0.f) {

                    }; else if (t.equals("int")) tree.init = new JCLiteral(TypeTags.INT, 0) {

                    }; else if (t.equals("long")) tree.init = new JCLiteral(TypeTags.LONG, 0) {

                    }; else if (t.equals("short")) tree.init = new JCLiteral(TypeTags.SHORT, 0) {

                    }; else tree.init = new JCLiteral(TypeTags.BOT, null) {

                    };

                }

            }

            result = tree;

        }
