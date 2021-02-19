package sample;

class Psi extends Interpolatable {
    private static final double[][][] c = {
            {{9.848868E-4, 2.99809, 3.14569, 3.98664, 4.9767, 5.99625, 7.01766, 8.01206, 9.00812, 10.0058, 11.0031, 12.0031, 13.0044, 13.6241}, {1.49797, 1.49796, 1.49537, 1.48263, 1.45343, 1.42209, 1.3843, 1.34005, 1.29006, 1.23434, 1.18006, 1.11646, 1.04855, 1.00208}},
            {{9.848868E-4, 1.83108, 1.90062, 1.99911, 2.10049, 2.19899, 2.30038, 2.40179, 2.50031, 2.60171, 2.70024, 2.79876, 2.90018, 2.99874, 3.07957, 3.99482, 5.00371, 6.01611, 6.97699, 7.18591}, {1.49797, 1.4996, 1.49612, 1.49197, 1.48927, 1.48512, 1.48098, 1.47469, 1.46838, 1.46281, 1.45507, 1.44876, 1.44175, 1.43042, 1.42439, 1.35432, 1.25989, 1.15328, 1.02515, 1.00154}},
            {{9.848868E-4, 1.05214, 1.1014, 1.19988, 1.30129, 1.39978, 1.5012, 1.59973, 1.70115, 1.80258, 1.90112, 1.99965, 2.09531, 2.19965, 2.30109, 2.39965, 2.5011, 2.59966, 2.69821, 2.79967, 2.90112, 2.9997, 3.10813, 4.01872, 4.93386}, {1.49797, 1.49829, 1.49478, 1.49207, 1.48649, 1.48235, 1.47533, 1.46831, 1.46058, 1.45284, 1.44439, 1.43593, 1.42603, 1.41614, 1.40697, 1.39636, 1.38575, 1.37514, 1.36524, 1.35463, 1.34403, 1.33126, 1.32547, 1.17153, 1.00181}},
            {{9.848868E-4, 0.916042, 1.00008, 1.1015, 1.20005, 1.3015, 1.40007, 1.50154, 1.60012, 1.7016, 1.8002, 1.89878, 2.00027, 2.09888, 2.20037, 2.30188, 2.39759, 2.502, 2.59773, 2.70215, 2.80078, 2.90232, 3.00096, 3.03115, 3.89476}, {1.49797, 1.49806, 1.49174, 1.48472, 1.47483, 1.46494, 1.45289, 1.44084, 1.42807, 1.41459, 1.39966, 1.38618, 1.37198, 1.35633, 1.34141, 1.32506, 1.30941, 1.29306, 1.27525, 1.25746, 1.23967, 1.21972, 1.20048, 1.19212, 1.00089}},
            {{9.848868E-4, 0.756771, 0.800255, 0.898792, 1.00024, 1.09881, 1.20029, 1.29888, 1.40039, 1.5019, 1.60052, 1.69915, 1.7978, 1.90224, 2.00092, 2.10251, 2.20119, 2.29988, 2.40149, 2.50023, 2.59893, 2.70347, 2.80223, 2.90104, 2.99999, 100.0}, {1.49797, 1.49851, 1.49356, 1.4851, 1.47521, 1.46316, 1.44968, 1.43619, 1.41983, 1.40348, 1.3864, 1.36788, 1.34792, 1.32798, 1.30515, 1.28017, 1.25662, 1.23236, 1.20594, 1.17665, 1.15094, 1.12022, 1.08949, 1.05229, 1.00144, 1.0}},
            {{9.848868E-4, 0.661206, 0.698899, 0.80038, 0.898966, 1.00047, 1.09908, 1.20061, 1.29925, 1.40081, 1.49947, 1.59817, 1.69976, 1.80135, 1.90007, 2.0017, 2.10047, 2.19927, 2.30094, 2.39974, 2.50145, 2.60026, 2.63513, 100.0}, {1.49797, 1.49907, 1.49411, 1.48062, 1.46714, 1.4515, 1.43514, 1.41662, 1.39739, 1.376, 1.35461, 1.32963, 1.30465, 1.28039, 1.25325, 1.22395, 1.19179, 1.15602, 1.12242, 1.08666, 1.04946, 1.01298, 1.00083, 1.0}},
            {{9.848868E-4, 0.611986, 0.771499, 0.899139, 1.00357, 1.09934, 1.20092, 1.29961, 1.39832, 1.49994, 1.5987, 1.70034, 1.79913, 1.90083, 1.99965, 2.10139, 2.20026, 2.30209, 100.0}, {1.49797, 1.49827, 1.47267, 1.44917, 1.42995, 1.40855, 1.38429, 1.36002, 1.33432, 1.30647, 1.27502, 1.24429, 1.20996, 1.1742, 1.13629, 1.09622, 1.05327, 1.00314, 1.0}},
            {{0.528003, 0.600528, 0.699155, 0.800699, 0.899389, 1.001, 1.09974, 1.19851, 1.30019, 1.39896, 1.50065, 1.59946, 1.70116, 1.8, 1.89884, 2.00058, 2.09941, 100.0}, {1.49885, 1.48532, 1.46752, 1.44757, 1.4233, 1.39617, 1.36687, 1.3347, 1.30038, 1.26821, 1.23317, 1.19598, 1.16022, 1.12014, 1.08007, 1.04, 1.00136, 1.0}},
            {{9.848868E-4, 0.441132, 0.502074, 0.600743, 0.699419, 0.798157, 0.899805, 0.998557, 1.10023, 1.20196, 1.30081, 1.39971, 1.49865, 1.60058, 1.69957, 1.74033, 100.0}, {1.49797, 1.4987, 1.48515, 1.46304, 1.44021, 1.41092, 1.38019, 1.34946, 1.31658, 1.27722, 1.23643, 1.18989, 1.13976, 1.07957, 1.02441, 1.00148, 1.0}},
            {{9.848868E-4, 0.380322, 0.499324, 0.598021, 0.699668, 0.801351, 0.900159, 0.999036, 1.10082, 1.1998, 1.30174, 1.40085, 1.49709, 100.0}, {1.49797, 1.4986, 1.47006, 1.44507, 1.41435, 1.38003, 1.34355, 1.29988, 1.2555, 1.20034, 1.14015, 1.07205, 1.00107, 1.0}},
            {{9.848868E-4, 0.200789, 0.310825, 0.400773, 0.499435, 0.60109, 0.699904, 0.80167, 0.900603, 0.999612, 1.1015, 1.20058, 1.29968, 1.37836, 100.0}, {1.49797, 1.4983, 1.49849, 1.47995, 1.45856, 1.42712, 1.38992, 1.34697, 1.29756, 1.24024, 1.18436, 1.11986, 1.0532, 1.00159, 1.0}},
            {{9.848868E-4, 0.200789, 0.302228, 0.400863, 0.499601, 0.554828, 0.601353, 0.700196, 0.79917, 0.901158, 1.00019, 1.09942, 1.19884, 100.0}, {1.49797, 1.4983, 1.48913, 1.47061, 1.44132, 1.41985, 1.39981, 1.35974, 1.30601, 1.24007, 1.17988, 1.10029, 0.999854, 1.0}},
            {{9.848868E-4, 0.200789, 0.302228, 0.400863, 0.499601, 0.554828, 0.601353, 0.700293, 0.80219, 0.901275, 1.00038, 1.09957, 1.19884, 100.0}, {1.49797, 1.4983, 1.48913, 1.47061, 1.44132, 1.41985, 1.39981, 1.34968, 1.29308, 1.22786, 1.16048, 1.08448, 0.999854, 1.0}}

    };
    private static final double[] pressure = {
            10.0, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 140
    };

    public static double get(double w,double pres){
        int bottomFirst = interpolate(pres,pressure);
        int upperFirst = bottomFirst == pressure.length-1?bottomFirst:bottomFirst+1;
        double[][] botArr =  c[bottomFirst];
        double[][] upArr =  c[upperFirst];
        double botC = botArr[1][interpolate(w,botArr[0])];
        double upC = upArr[1][interpolate(w,upArr[0])];
        return (upperFirst==bottomFirst)?botC:botC+((pressure[upperFirst]-pres)/(pressure[upperFirst]-pressure[bottomFirst]))*(upC-botC);
    }


}