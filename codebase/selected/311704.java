package com.googlecode.protobuf.socketrpc;

public final class TestProtos {

    private TestProtos() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public interface RequestOrBuilder extends com.google.protobuf.MessageOrBuilder {

        boolean hasStrData();

        String getStrData();
    }

    public static final class Request extends com.google.protobuf.GeneratedMessage implements RequestOrBuilder {

        private Request(Builder builder) {
            super(builder);
        }

        private Request(boolean noInit) {
        }

        private static final Request defaultInstance;

        public static Request getDefaultInstance() {
            return defaultInstance;
        }

        public Request getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Request_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Request_fieldAccessorTable;
        }

        private int bitField0_;

        public static final int STR_DATA_FIELD_NUMBER = 1;

        private Object strData_;

        public boolean hasStrData() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        public String getStrData() {
            Object ref = strData_;
            if (ref instanceof String) {
                return (String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                if (com.google.protobuf.Internal.isValidUtf8(bs)) {
                    strData_ = s;
                }
                return s;
            }
        }

        private com.google.protobuf.ByteString getStrDataBytes() {
            Object ref = strData_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8((String) ref);
                strData_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            strData_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;
            if (!hasStrData()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getStrDataBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getStrDataBytes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        @java.lang.Override
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Request parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.googlecode.protobuf.socketrpc.TestProtos.Request prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements com.googlecode.protobuf.socketrpc.TestProtos.RequestOrBuilder {

            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Request_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Request_fieldAccessorTable;
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                strData_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.googlecode.protobuf.socketrpc.TestProtos.Request.getDescriptor();
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Request getDefaultInstanceForType() {
                return com.googlecode.protobuf.socketrpc.TestProtos.Request.getDefaultInstance();
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Request build() {
                com.googlecode.protobuf.socketrpc.TestProtos.Request result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            private com.googlecode.protobuf.socketrpc.TestProtos.Request buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                com.googlecode.protobuf.socketrpc.TestProtos.Request result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return result;
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Request buildPartial() {
                com.googlecode.protobuf.socketrpc.TestProtos.Request result = new com.googlecode.protobuf.socketrpc.TestProtos.Request(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.strData_ = strData_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.googlecode.protobuf.socketrpc.TestProtos.Request) {
                    return mergeFrom((com.googlecode.protobuf.socketrpc.TestProtos.Request) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.googlecode.protobuf.socketrpc.TestProtos.Request other) {
                if (other == com.googlecode.protobuf.socketrpc.TestProtos.Request.getDefaultInstance()) return this;
                if (other.hasStrData()) {
                    setStrData(other.getStrData());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasStrData()) {
                    return false;
                }
                return true;
            }

            public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
                while (true) {
                    int tag = input.readTag();
                    switch(tag) {
                        case 0:
                            this.setUnknownFields(unknownFields.build());
                            onChanged();
                            return this;
                        default:
                            {
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    this.setUnknownFields(unknownFields.build());
                                    onChanged();
                                    return this;
                                }
                                break;
                            }
                        case 10:
                            {
                                bitField0_ |= 0x00000001;
                                strData_ = input.readBytes();
                                break;
                            }
                    }
                }
            }

            private int bitField0_;

            private Object strData_ = "";

            public boolean hasStrData() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            public String getStrData() {
                Object ref = strData_;
                if (!(ref instanceof String)) {
                    String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    strData_ = s;
                    return s;
                } else {
                    return (String) ref;
                }
            }

            public Builder setStrData(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                strData_ = value;
                onChanged();
                return this;
            }

            public Builder clearStrData() {
                bitField0_ = (bitField0_ & ~0x00000001);
                strData_ = getDefaultInstance().getStrData();
                onChanged();
                return this;
            }

            void setStrData(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000001;
                strData_ = value;
                onChanged();
            }
        }

        static {
            defaultInstance = new Request(true);
            defaultInstance.initFields();
        }
    }

    public interface ResponseOrBuilder extends com.google.protobuf.MessageOrBuilder {

        boolean hasStrData();

        String getStrData();

        boolean hasIntData();

        int getIntData();
    }

    public static final class Response extends com.google.protobuf.GeneratedMessage implements ResponseOrBuilder {

        private Response(Builder builder) {
            super(builder);
        }

        private Response(boolean noInit) {
        }

        private static final Response defaultInstance;

        public static Response getDefaultInstance() {
            return defaultInstance;
        }

        public Response getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Response_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Response_fieldAccessorTable;
        }

        private int bitField0_;

        public static final int STR_DATA_FIELD_NUMBER = 1;

        private Object strData_;

        public boolean hasStrData() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        public String getStrData() {
            Object ref = strData_;
            if (ref instanceof String) {
                return (String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                if (com.google.protobuf.Internal.isValidUtf8(bs)) {
                    strData_ = s;
                }
                return s;
            }
        }

        private com.google.protobuf.ByteString getStrDataBytes() {
            Object ref = strData_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8((String) ref);
                strData_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        public static final int INT_DATA_FIELD_NUMBER = 2;

        private int intData_;

        public boolean hasIntData() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        public int getIntData() {
            return intData_;
        }

        private void initFields() {
            strData_ = "";
            intData_ = 0;
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;
            if (!hasStrData()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getStrDataBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeInt32(2, intData_);
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getStrDataBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(2, intData_);
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        @java.lang.Override
        protected Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.googlecode.protobuf.socketrpc.TestProtos.Response parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.googlecode.protobuf.socketrpc.TestProtos.Response prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> implements com.googlecode.protobuf.socketrpc.TestProtos.ResponseOrBuilder {

            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Response_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return com.googlecode.protobuf.socketrpc.TestProtos.internal_static_protobuf_socketrpc_Response_fieldAccessorTable;
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                strData_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                intData_ = 0;
                bitField0_ = (bitField0_ & ~0x00000002);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.googlecode.protobuf.socketrpc.TestProtos.Response.getDescriptor();
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Response getDefaultInstanceForType() {
                return com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance();
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Response build() {
                com.googlecode.protobuf.socketrpc.TestProtos.Response result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            private com.googlecode.protobuf.socketrpc.TestProtos.Response buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                com.googlecode.protobuf.socketrpc.TestProtos.Response result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return result;
            }

            public com.googlecode.protobuf.socketrpc.TestProtos.Response buildPartial() {
                com.googlecode.protobuf.socketrpc.TestProtos.Response result = new com.googlecode.protobuf.socketrpc.TestProtos.Response(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.strData_ = strData_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.intData_ = intData_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.googlecode.protobuf.socketrpc.TestProtos.Response) {
                    return mergeFrom((com.googlecode.protobuf.socketrpc.TestProtos.Response) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.googlecode.protobuf.socketrpc.TestProtos.Response other) {
                if (other == com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance()) return this;
                if (other.hasStrData()) {
                    setStrData(other.getStrData());
                }
                if (other.hasIntData()) {
                    setIntData(other.getIntData());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasStrData()) {
                    return false;
                }
                return true;
            }

            public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
                while (true) {
                    int tag = input.readTag();
                    switch(tag) {
                        case 0:
                            this.setUnknownFields(unknownFields.build());
                            onChanged();
                            return this;
                        default:
                            {
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    this.setUnknownFields(unknownFields.build());
                                    onChanged();
                                    return this;
                                }
                                break;
                            }
                        case 10:
                            {
                                bitField0_ |= 0x00000001;
                                strData_ = input.readBytes();
                                break;
                            }
                        case 16:
                            {
                                bitField0_ |= 0x00000002;
                                intData_ = input.readInt32();
                                break;
                            }
                    }
                }
            }

            private int bitField0_;

            private Object strData_ = "";

            public boolean hasStrData() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            public String getStrData() {
                Object ref = strData_;
                if (!(ref instanceof String)) {
                    String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    strData_ = s;
                    return s;
                } else {
                    return (String) ref;
                }
            }

            public Builder setStrData(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                strData_ = value;
                onChanged();
                return this;
            }

            public Builder clearStrData() {
                bitField0_ = (bitField0_ & ~0x00000001);
                strData_ = getDefaultInstance().getStrData();
                onChanged();
                return this;
            }

            void setStrData(com.google.protobuf.ByteString value) {
                bitField0_ |= 0x00000001;
                strData_ = value;
                onChanged();
            }

            private int intData_;

            public boolean hasIntData() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            public int getIntData() {
                return intData_;
            }

            public Builder setIntData(int value) {
                bitField0_ |= 0x00000002;
                intData_ = value;
                onChanged();
                return this;
            }

            public Builder clearIntData() {
                bitField0_ = (bitField0_ & ~0x00000002);
                intData_ = 0;
                onChanged();
                return this;
            }
        }

        static {
            defaultInstance = new Response(true);
            defaultInstance.initFields();
        }
    }

    public abstract static class TestService implements com.google.protobuf.Service {

        protected TestService() {
        }

        public interface Interface {

            public abstract void testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request, com.google.protobuf.RpcCallback<com.googlecode.protobuf.socketrpc.TestProtos.Response> done);
        }

        public static com.google.protobuf.Service newReflectiveService(final Interface impl) {
            return new TestService() {

                @java.lang.Override
                public void testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request, com.google.protobuf.RpcCallback<com.googlecode.protobuf.socketrpc.TestProtos.Response> done) {
                    impl.testMethod(controller, request, done);
                }
            };
        }

        public static com.google.protobuf.BlockingService newReflectiveBlockingService(final BlockingInterface impl) {
            return new com.google.protobuf.BlockingService() {

                public final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public final com.google.protobuf.Message callBlockingMethod(com.google.protobuf.Descriptors.MethodDescriptor method, com.google.protobuf.RpcController controller, com.google.protobuf.Message request) throws com.google.protobuf.ServiceException {
                    if (method.getService() != getDescriptor()) {
                        throw new java.lang.IllegalArgumentException("Service.callBlockingMethod() given method descriptor for " + "wrong service type.");
                    }
                    switch(method.getIndex()) {
                        case 0:
                            return impl.testMethod(controller, (com.googlecode.protobuf.socketrpc.TestProtos.Request) request);
                        default:
                            throw new java.lang.AssertionError("Can't get here.");
                    }
                }

                public final com.google.protobuf.Message getRequestPrototype(com.google.protobuf.Descriptors.MethodDescriptor method) {
                    if (method.getService() != getDescriptor()) {
                        throw new java.lang.IllegalArgumentException("Service.getRequestPrototype() given method " + "descriptor for wrong service type.");
                    }
                    switch(method.getIndex()) {
                        case 0:
                            return com.googlecode.protobuf.socketrpc.TestProtos.Request.getDefaultInstance();
                        default:
                            throw new java.lang.AssertionError("Can't get here.");
                    }
                }

                public final com.google.protobuf.Message getResponsePrototype(com.google.protobuf.Descriptors.MethodDescriptor method) {
                    if (method.getService() != getDescriptor()) {
                        throw new java.lang.IllegalArgumentException("Service.getResponsePrototype() given method " + "descriptor for wrong service type.");
                    }
                    switch(method.getIndex()) {
                        case 0:
                            return com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance();
                        default:
                            throw new java.lang.AssertionError("Can't get here.");
                    }
                }
            };
        }

        public abstract void testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request, com.google.protobuf.RpcCallback<com.googlecode.protobuf.socketrpc.TestProtos.Response> done);

        public static final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptor() {
            return com.googlecode.protobuf.socketrpc.TestProtos.getDescriptor().getServices().get(0);
        }

        public final com.google.protobuf.Descriptors.ServiceDescriptor getDescriptorForType() {
            return getDescriptor();
        }

        public final void callMethod(com.google.protobuf.Descriptors.MethodDescriptor method, com.google.protobuf.RpcController controller, com.google.protobuf.Message request, com.google.protobuf.RpcCallback<com.google.protobuf.Message> done) {
            if (method.getService() != getDescriptor()) {
                throw new java.lang.IllegalArgumentException("Service.callMethod() given method descriptor for wrong " + "service type.");
            }
            switch(method.getIndex()) {
                case 0:
                    this.testMethod(controller, (com.googlecode.protobuf.socketrpc.TestProtos.Request) request, com.google.protobuf.RpcUtil.<com.googlecode.protobuf.socketrpc.TestProtos.Response>specializeCallback(done));
                    return;
                default:
                    throw new java.lang.AssertionError("Can't get here.");
            }
        }

        public final com.google.protobuf.Message getRequestPrototype(com.google.protobuf.Descriptors.MethodDescriptor method) {
            if (method.getService() != getDescriptor()) {
                throw new java.lang.IllegalArgumentException("Service.getRequestPrototype() given method " + "descriptor for wrong service type.");
            }
            switch(method.getIndex()) {
                case 0:
                    return com.googlecode.protobuf.socketrpc.TestProtos.Request.getDefaultInstance();
                default:
                    throw new java.lang.AssertionError("Can't get here.");
            }
        }

        public final com.google.protobuf.Message getResponsePrototype(com.google.protobuf.Descriptors.MethodDescriptor method) {
            if (method.getService() != getDescriptor()) {
                throw new java.lang.IllegalArgumentException("Service.getResponsePrototype() given method " + "descriptor for wrong service type.");
            }
            switch(method.getIndex()) {
                case 0:
                    return com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance();
                default:
                    throw new java.lang.AssertionError("Can't get here.");
            }
        }

        public static Stub newStub(com.google.protobuf.RpcChannel channel) {
            return new Stub(channel);
        }

        public static final class Stub extends com.googlecode.protobuf.socketrpc.TestProtos.TestService implements Interface {

            private Stub(com.google.protobuf.RpcChannel channel) {
                this.channel = channel;
            }

            private final com.google.protobuf.RpcChannel channel;

            public com.google.protobuf.RpcChannel getChannel() {
                return channel;
            }

            public void testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request, com.google.protobuf.RpcCallback<com.googlecode.protobuf.socketrpc.TestProtos.Response> done) {
                channel.callMethod(getDescriptor().getMethods().get(0), controller, request, com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance(), com.google.protobuf.RpcUtil.generalizeCallback(done, com.googlecode.protobuf.socketrpc.TestProtos.Response.class, com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance()));
            }
        }

        public static BlockingInterface newBlockingStub(com.google.protobuf.BlockingRpcChannel channel) {
            return new BlockingStub(channel);
        }

        public interface BlockingInterface {

            public com.googlecode.protobuf.socketrpc.TestProtos.Response testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request) throws com.google.protobuf.ServiceException;
        }

        private static final class BlockingStub implements BlockingInterface {

            private BlockingStub(com.google.protobuf.BlockingRpcChannel channel) {
                this.channel = channel;
            }

            private final com.google.protobuf.BlockingRpcChannel channel;

            public com.googlecode.protobuf.socketrpc.TestProtos.Response testMethod(com.google.protobuf.RpcController controller, com.googlecode.protobuf.socketrpc.TestProtos.Request request) throws com.google.protobuf.ServiceException {
                return (com.googlecode.protobuf.socketrpc.TestProtos.Response) channel.callBlockingMethod(getDescriptor().getMethods().get(0), controller, request, com.googlecode.protobuf.socketrpc.TestProtos.Response.getDefaultInstance());
            }
        }
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_protobuf_socketrpc_Request_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_protobuf_socketrpc_Request_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_protobuf_socketrpc_Response_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_protobuf_socketrpc_Response_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = { "\n?java/src/test/java/com/googlecode/prot" + "obuf/socketrpc/test.proto\022\022protobuf.sock" + "etrpc\"\033\n\007Request\022\020\n\010str_data\030\001 \002(\t\".\n\010Re" + "sponse\022\020\n\010str_data\030\001 \002(\t\022\020\n\010int_data\030\002 \001" + "(\0052V\n\013TestService\022G\n\nTestMethod\022\033.protob" + "uf.socketrpc.Request\032\034.protobuf.socketrp" + "c.ResponseB2\n!com.googlecode.protobuf.so" + "cketrpcB\nTestProtos\210\001\001" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                internal_static_protobuf_socketrpc_Request_descriptor = getDescriptor().getMessageTypes().get(0);
                internal_static_protobuf_socketrpc_Request_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_protobuf_socketrpc_Request_descriptor, new java.lang.String[] { "StrData" }, com.googlecode.protobuf.socketrpc.TestProtos.Request.class, com.googlecode.protobuf.socketrpc.TestProtos.Request.Builder.class);
                internal_static_protobuf_socketrpc_Response_descriptor = getDescriptor().getMessageTypes().get(1);
                internal_static_protobuf_socketrpc_Response_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_protobuf_socketrpc_Response_descriptor, new java.lang.String[] { "StrData", "IntData" }, com.googlecode.protobuf.socketrpc.TestProtos.Response.class, com.googlecode.protobuf.socketrpc.TestProtos.Response.Builder.class);
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
    }
}
