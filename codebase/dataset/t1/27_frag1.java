                case '\\':

                    buf.append("\\5c");

                    break;

                case '*':

                    buf.append("\\2a");

                    break;

                case '(':

                    buf.append("\\28");

                    break;

                case ')':

                    buf.append("\\29");

                    break;

                case '\0':

                    buf.append("\\00");

                    break;

                default:
