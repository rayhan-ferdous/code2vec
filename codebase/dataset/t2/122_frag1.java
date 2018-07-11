                    r = Z_DATA_ERROR;

                    s.bitb = b;

                    s.bitk = k;

                    z.avail_in = n;

                    z.total_in += p - z.next_in_index;

                    z.next_in_index = p;

                    s.write = q;

                    return s.inflate_flush(z, r);

                case LENEXT:

                    j = get;

                    while (k < j) {

                        if (n != 0) {

                            r = Z_OK;

                        } else {

                            s.bitb = b;

                            s.bitk = k;

                            z.avail_in = n;

                            z.total_in += p - z.next_in_index;

                            z.next_in_index = p;

                            s.write = q;

                            return s.inflate_flush(z, r);

                        }
