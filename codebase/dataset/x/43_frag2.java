                        read_insert.limit(old_limit);

                    } else {

                        buffer.put(read_insert);

                    }

                    if (!read_insert.hasRemaining()) {

                        break;

                    }

                }

            }

            total_read = read_insert.position() - pos_before;

            if (read_insert.hasRemaining()) {

                return (total_read);
