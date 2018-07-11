                                request2.setName(child.getName());

                                if ((child instanceof RegisteredInterface) && ((nChild = ((RegisteredInterface) child).getRegistered(request2)) != null)) {

                                    rmapR.addChild(nChild);
