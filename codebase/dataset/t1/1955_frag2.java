                                bitk = k;

                                z.avail_in = n;

                                z.total_in += p - z.next_in_index;

                                z.next_in_index = p;

                                write = q;

                                return inflate_flush(z, r);

                            }

                            ;

                            n--;

                            b |= (z.next_in[p++] & 0xff) << k;

                            k += 8;

                        }
