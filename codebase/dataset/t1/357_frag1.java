                    dcmdir.query();

                    break;

                case 'a':

                    dcmdir.append(args, g.getOptind());

                    break;

                case 'x':

                case 'X':

                    dcmdir.remove(args, g.getOptind(), cmd == 'X');

                    break;

                case 'z':

                    dcmdir.compact();

                    break;

                case 'P':

                    dcmdir.purge();

                    break;

                default:

                    throw new RuntimeException();

            }

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            exit(e.getMessage(), true);

        }

    }
