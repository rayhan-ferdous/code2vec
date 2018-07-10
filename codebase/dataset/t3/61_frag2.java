                            stack.push(a._truediv(b));

                            break;

                        }

                    case Opcode.BINARY_FLOOR_DIVIDE:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._floordiv(b));

                            break;

                        }

                    case Opcode.BINARY_MODULO:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._mod(b));

                            break;

                        }

                    case Opcode.BINARY_ADD:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._add(b));

                            break;

                        }

                    case Opcode.BINARY_SUBTRACT:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._sub(b));

                            break;

                        }

                    case Opcode.BINARY_SUBSCR:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a.__getitem__(b));

                            break;

                        }

                    case Opcode.BINARY_LSHIFT:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._lshift(b));

                            break;

                        }

                    case Opcode.BINARY_RSHIFT:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._rshift(b));

                            break;

                        }

                    case Opcode.BINARY_AND:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._and(b));

                            break;

                        }

                    case Opcode.BINARY_XOR:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._xor(b));

                            break;

                        }

                    case Opcode.BINARY_OR:

                        {

                            PyObject b = stack.pop();

                            PyObject a = stack.pop();

                            stack.push(a._or(b));

                            break;

                        }

                    case Opcode.LIST_APPEND:

                        {

                            PyObject b = stack.pop();
