package tech.xfs.xfsgolibj.utils;

public class Binary {
    public static class LittleEndian {
        public static int toInt16(byte[] bs) {
            return bs[0] & 0xff |
                    (bs[1] & 0xff) << 8;
        }
        public static int toInt32(byte[] bs) {
            return bs[0] & 0xff |
                    (bs[1] & 0xff) << 8 |
                    (bs[2] & 0xff) << 16 |
                    (bs[3] & 0xff) << 24;
        }
        public static long toInt64(byte[] bs) {
            return (long) bs[0] & 0xff |
                    ((long) bs[1] & 0xff) << 8 |
                    ((long) bs[2] & 0xff) << 16 |
                    ((long) bs[3] & 0xff) << 24 |
                    ((long) bs[4] & 0xff) << 32 |
                    ((long) bs[5] & 0xff) << 40 |
                    ((long) bs[6] & 0xff) << 48 |
                    ((long) bs[7] & 0xff) << 56;
        }
        public static byte[] fromInt16(int num){
            byte[] bs = new byte[2];
            bs[0] = (byte) (num & 0xff);
            bs[1] = (byte) ((num >> 8) & 0xff);
            return bs;
        }
        public static byte[] fromInt32(int num){
            byte[] bs = new byte[4];
            bs[0] = (byte) (num & 0xff);
            bs[1] = (byte) ((num >> 8) & 0xff);
            bs[2] = (byte) ((num >> 16) & 0xff);
            bs[3] = (byte) ((num >> 24) & 0xff);
            return bs;
        }

        public static byte[] fromInt64(long num){
            byte[] bs = new byte[8];
            bs[0] = (byte) (num & 0xff);
            bs[1] = (byte) ((num >> 8) & 0xff);
            bs[2] = (byte) ((num >> 16) & 0xff);
            bs[3] = (byte) ((num >> 24) & 0xff);
            bs[4] = (byte) ((num >> 32) & 0xff);
            bs[5] = (byte) ((num >> 40) & 0xff);
            bs[6] = (byte) ((num >> 48) & 0xff);
            bs[7] = (byte) ((num >> 56) & 0xff);
            return bs;
        }
    }
}
