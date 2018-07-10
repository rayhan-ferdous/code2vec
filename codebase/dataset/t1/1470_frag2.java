                            t += (b & inflate_mask[e]);

                            tp_index_t_3 = (tp_index + t) * 3;

                            e = tp[tp_index_t_3];

                        } else {

                            z.msg = "invalid distance code";

                            c = z.avail_in - n;

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

                    break;

                }

                if ((e & 64) == 0) {

                    t += tp[tp_index_t_3 + 2];

                    t += (b & inflate_mask[e]);

                    tp_index_t_3 = (tp_index + t) * 3;

                    if ((e = tp[tp_index_t_3]) == 0) {

                        b >>= (tp[tp_index_t_3 + 1]);

                        k -= (tp[tp_index_t_3 + 1]);

                        s.window[q++] = (byte) tp[tp_index_t_3 + 2];

                        m--;

                        break;

                    }

                } else if ((e & 32) != 0) {

                    c = z.avail_in - n;

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

                    return Z_STREAM_END;
