                            c = (k >> 3) < c ? k >> 3 : c;

                            n += c;

                            p -= c;

                            k -= c << 3;

                            s.bitb = b;

                            s.bitk = k;

                            z.avail_in = n;

                            z.total_in += p - z.next_in_index;

                            z.next_in_index = p;

                            s.write = q;

                            return Z_DATA_ERROR;

                        }

                    } while (true);
