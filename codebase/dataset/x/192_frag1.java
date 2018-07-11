                if (o instanceof SubsystemNode) {

                    SubsystemNode node = (SubsystemNode) o;

                    Subsystem sub = node.getMsubsystem();

                    if (sub != null) subsInDiagram.put(sub, node);

                    if (msub == sub) {

                        evt.detail = DND.DROP_NONE;

                        return;
