    private static void readActions(GmFileContext c, ActionContainer container, String errorKey, int format1, int format2) throws IOException, GmFormatException {

        final GmFile f = c.f;

        GmStreamDecoder in = c.in;

        int ver = in.read4();

        if (ver != 400) {

            throw new GmFormatException(f, Messages.format("GmFileReader.ERROR_UNSUPPORTED", Messages.format("GmFileReader." + errorKey, format1, format2), ver));

        }

        int noacts = in.read4();

        for (int k = 0; k < noacts; k++) {

            in.skip(4);

            int libid = in.read4();

            int actid = in.read4();

            LibAction la = LibManager.getLibAction(libid, actid);

            boolean unknownLib = la == null;

            if (unknownLib) {

                la = new LibAction();

                la.id = actid;

                la.parentId = libid;

                la.actionKind = (byte) in.read4();

                la.allowRelative = in.readBool();

                la.question = in.readBool();

                la.canApplyTo = in.readBool();

                la.execType = (byte) in.read4();

                if (la.execType == Action.EXEC_FUNCTION) la.execInfo = in.readStr(); else in.skip(in.read4());

                if (la.execType == Action.EXEC_CODE) la.execInfo = in.readStr(); else in.skip(in.read4());

            } else {

                in.skip(20);

                in.skip(in.read4());

                in.skip(in.read4());

            }

            Argument[] args = new Argument[in.read4()];

            byte[] argkinds = new byte[in.read4()];

            for (int x = 0; x < argkinds.length; x++) argkinds[x] = (byte) in.read4();

            if (unknownLib) {

                la.libArguments = new LibArgument[argkinds.length];

                for (int x = 0; x < argkinds.length; x++) {

                    la.libArguments[x] = new LibArgument();

                    la.libArguments[x].kind = argkinds[x];

                }

            }

            Action act = container.addAction(la);

            int appliesTo = in.read4();

            switch(appliesTo) {

                case -1:

                    act.setAppliesTo(GmObject.OBJECT_SELF);

                    break;

                case -2:

                    act.setAppliesTo(GmObject.OBJECT_OTHER);

                    break;

                default:

                    act.setAppliesTo(c.objids.get(appliesTo));

            }

            act.setRelative(in.readBool());

            int actualnoargs = in.read4();

            for (int l = 0; l < actualnoargs; l++) {

                if (l >= args.length) {

                    in.skip(in.read4());

                    continue;

                }

                args[l] = new Argument(argkinds[l]);

                String strval = in.readStr();

                args[l].setVal(strval);

                Class<? extends Resource<?, ?>> kind = Argument.getResourceKind(argkinds[l]);

                if (kind != null && Resource.class.isAssignableFrom(kind)) try {

                    final int id = Integer.parseInt(strval);

                    final Argument arg = args[l];

                    PostponedRef pr = new PostponedRef() {



                        public boolean invoke() {

                            ResourceHolder<?> rh = f.resMap.get(Argument.getResourceKind(arg.kind));

                            Resource<?, ?> temp = null;

                            if (rh instanceof ResourceList<?>) temp = ((ResourceList<?>) rh).getUnsafe(id); else temp = rh.getResource();

                            if (temp != null) arg.setRes(temp.reference);

                            return temp != null;

                        }

                    };

                    if (!pr.invoke()) postpone.add(pr);

                } catch (NumberFormatException e) {

                }

                act.setArguments(args);

            }

            act.setNot(in.readBool());

        }

    }
