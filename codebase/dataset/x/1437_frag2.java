        } catch (final IOException e) {

            e.printStackTrace();

        }

    }



    protected ByteBuffer createBuffer() {

        if (buffer_ == null) {

            buffer_ = ByteBuffer.allocateDirect(4 * nbValues_);

            if (order_ != null) {

                buffer_.order(order_);

            }

        }

        return buffer_;
