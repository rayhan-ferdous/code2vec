            public TType call(ExecutionContext inContext, TObject inThis, TType[] inArgs) throws ScriptException {

                if (inArgs.length < 2) {

                    throw new ScriptException(ScriptException.Type.ERROR, "Two arguments expected.", inContext.getLineNumber());

                }

                String type = inArgs[0].toJSString(inContext).toString();

                String url = inArgs[1].toJSString(inContext).toString();

                boolean async = false;

                if (inArgs.length >= 3) {

                    async = inArgs[2].toJSBoolean().toBooleanValue();

                }

                ((TXmlHttpRequest) inThis).open(inContext, type, url, async);

                return TUndefined.INSTANCE;

            }
